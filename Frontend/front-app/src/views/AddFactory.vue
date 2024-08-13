<template>
  <div class="container">
    <button @click="goBackToFactoryList" class="btn-back">Back to Factories</button>
    <h1 class="main-title">Add New Factory</h1>
    <form @submit.prevent="addFactory" class="factory-form">
      <div class="form-group">
        <label>Name:</label>
        <input type="text" v-model="newFactory.name" required class="form-control">
      </div>
      <div class="form-group">
        <label>Street:</label>
        <input type="text" v-model="newFactory.location.street" required class="form-control">
      </div>
      <div class="form-group">
        <label>City:</label>
        <input type="text" v-model="newFactory.location.city" required class="form-control">
      </div>
      <div class="form-group">
        <label>State:</label>
        <input type="text" v-model="newFactory.location.state" required class="form-control">
      </div>
      <div class="form-group">
        <label>Latitude:</label>
        <input type="number" v-model="newFactory.location.latitude" step="0.000001" readonly class="form-control">
      </div>
      <div class="form-group">
        <label>Longitude:</label>
        <input type="number" v-model="newFactory.location.longitude" step="0.000001" readonly class="form-control">
      </div>
      <div id="map" class="map"></div>
      <div class="form-group">
        <label>Working Hours:</label>
        <input type="text" v-model="newFactory.workingHours" placeholder="HH:mm-HH:mm" required class="form-control">
        <span v-if="workingHoursError" class="error-message">{{ workingHoursError }}</span>
      </div>
      <div class="form-group">
        <label>Work Status:</label>
        <select v-model="newFactory.workStatus" required class="form-control">
          <option value="Working">Working</option>
          <option value="Closed">Closed</option>
        </select>
      </div>
      <div class="form-group">
        <label>Manager:</label>
        <div v-if="managers.length > 0">
          <select v-model="selectedManager" class="form-control">
            <option v-for="manager in managers" :key="manager.username" :value="manager.username">{{ manager.firstName }} {{ manager.lastName }}</option>
          </select>
        </div>
        <div v-else>
          <button @click="goToSignUpManager" class="btn-add-manager">Add Manager</button>
        </div>
      </div>
      <div class="form-group">
        <label>Logo:</label>
        <input type="file" @change="onFileChange" class="form-control">
        <img v-if="previewImage" :src="previewImage" class="preview-image" />
      </div>
      <div class="form-buttons">
        <button type="submit" class="btn-primary">Add Factory</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import { Tile as TileLayer } from 'ol/layer';
import { OSM as OSMSource } from 'ol/source';
import { fromLonLat, toLonLat } from 'ol/proj';
import { Style, Icon } from 'ol/style';
import { Feature } from 'ol';
import Point from 'ol/geom/Point';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';

export default {
  data() {
    return {
      previewImage: null,
      map: null,
      markerLayer: null,
      workingHoursError: null,
      openCageApiKey: '2442110cc01e45238bd53f0f451c7a3a',
      managers: []
    };
  },
  computed: {
    newFactory() {
      return this.$store.state.factoryForm;
    },
  },
  mounted() {
    this.initMap();
    this.fetchManagers();
  },
  methods: {
    initMap() {
      this.map = new Map({
        target: 'map',
        layers: [
          new TileLayer({
            source: new OSMSource(),
          }),
        ],
        view: new View({
          center: fromLonLat([0, 0]),
          zoom: 2,
        }),
      });

      this.markerLayer = new VectorLayer({
        source: new VectorSource(),
        style: new Style({
          image: new Icon({
            anchor: [0.5, 1],
            src: 'https://openlayers.org/en/v4.6.5/examples/data/icon.png',
          }),
        }),
      });

      this.map.addLayer(this.markerLayer);

      this.map.on('singleclick', (event) => {
        const coordinate = toLonLat(event.coordinate);
        this.$store.commit('setFactoryForm', {
          ...this.newFactory,
          location: {
            ...this.newFactory.location,
            latitude: coordinate[1],
            longitude: coordinate[0],
          },
        });

        this.addMarker(event.coordinate);
        this.reverseGeocode(coordinate[1], coordinate[0]);
      });
    },
    addMarker(coordinate) {
      const marker = new Feature({
        geometry: new Point(coordinate),
      });

      this.markerLayer.getSource().clear();
      this.markerLayer.getSource().addFeature(marker);
    },
    reverseGeocode(lat, lon) {
      axios
        .get(`https://api.opencagedata.com/geocode/v1/json?q=${lat}+${lon}&key=${this.openCageApiKey}`)
        .then((response) => {
          const components = response.data.results[0].components;
          this.$store.commit('setFactoryForm', {
            ...this.newFactory,
            location: {
              ...this.newFactory.location,
              street: components.road || '',
              city: components.city || components.town || components.village || '',
              state: components.state || '',
            },
          });
        })
        .catch((error) => {
          console.error('Error during reverse geocoding', error);
        });
    },
    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImage = e.target.result;
          this.$store.commit('setFactoryForm', {
            ...this.newFactory,
            logo: file.name,
          });
        };
        reader.readAsDataURL(file);
      }
    },
    validateWorkingHours() {
      const regex = /^([01]\d|2[0-3]):([0-5]\d)-([01]\d|2[0-3]):([0-5]\d)$/;
      if (!regex.test(this.newFactory.workingHours)) {
        this.workingHoursError = 'Working hours must be in the format HH:mm-HH:mm';
        return false;
      }
      this.workingHoursError = null;
      return true;
    },
    async addFactory() {
    if (this.validateWorkingHours()) {
      try {
        const response = await axios.post('http://localhost:8080/WebShopAppREST/rest/factories/addFactory', this.newFactory);
        if (this.newFactory.selectedManager) {
          await this.assignManager(response.data.id, this.newFactory.selectedManager);
        } else {
          alert('Factory added successfully');
          this.$store.commit('resetFactoryForm'); 
          this.previewImage = null; 
          this.$router.push({
            name: 'FactoryListAdministrator',
            params: { username: this.$route.params.username, factoryId: this.$route.params.id },
          });
        }
      } catch (error) {
        console.error('Error adding factory', error);
      }
    }
  },
    fetchManagers() {
      axios
        .get('http://localhost:8080/WebShopAppREST/rest/users/managersWithoutFactory')
        .then((response) => {
          this.managers = response.data;
        })
        .catch((error) => {
          console.error('Error fetching managers', error);
        });
    },
    async assignManager(factoryId, managerUsername) {
    try {
      await axios.put(`http://localhost:8080/WebShopAppREST/rest/users/${managerUsername}/assignFactory/${factoryId}`);
      alert('Factory and manager added successfully');
      this.$store.commit('resetFactoryForm');
      this.previewImage = null;
      this.$router.push({
        name: 'FactoryListAdministrator',
        params: { username: this.$route.params.username, factoryId: this.$route.params.id },
      });
    } catch (error) {
      console.error('Error assigning manager', error);
    }
  },
    goToSignUpManager() {
      this.$store.commit('setFactoryForm', this.newFactory);
      this.$router.push({ name: 'SignUpManager'});
    },
    goBackToFactoryList() {
      this.$router.push({
        name: 'FactoryListAdministrator',
        params: { username: this.$route.params.username, factoryId: this.$route.params.id },
      });
    },
  },
};
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&display=swap');

.container {
  font-family: 'Open Sans', sans-serif;
  background: #f9f9f9;
  padding: 20px;
}

.btn-back {
  background-color: #6b3e26;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 10px;
}

.main-title {
  text-align: center;
  font-size: 24px;
  color: #6b3e26;
}

.factory-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: 600;
  margin-bottom: 5px;
  color: #6b3e26;
}

.form-control {
  width: 100%;
  padding: 8px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.map {
  width: 100%;
  height: 300px;
  margin-bottom: 20px;
  border-radius: 5px;
  overflow: hidden;
}

.error-message {
  color: red;
  font-size: 12px;
}

.btn-primary {
  background-color: #6b3e26;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.preview-image {
  max-width: 100px;
  margin-top: 10px;
  border-radius: 5px;
}

.btn-add-manager {
  background-color: #6b3e26;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
