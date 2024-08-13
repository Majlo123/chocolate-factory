<template>
  <div class="container">
    <div class="header">
      <h1 class="main-title">Chocolate Factories</h1>
      <router-link 
        v-if="user && selectedFactoryId" 
        :to="{ name: 'FactoryListManager', params: { username: user.username, factoryId: selectedFactoryId } }">Home</router-link>
      <router-link 
        v-if="user && selectedFactoryId" 
        :to="{ name: 'FactoryDetailsManager', params: { username: user.username, factoryId: selectedFactoryId } }">
        Edit factory
      </router-link>
      <router-link 
        v-if="user && selectedFactoryId" 
        :to="{ name: 'ViewProfileManager', params: { username: user.username, factoryId: selectedFactoryId } }">View Profile</router-link>
      <div class="auth">
        <span v-if="user">Welcome, {{ user.firstName }} {{ user.lastName }}!</span>
        <router-link v-else to="/sign-in">Sign In</router-link>
        <button v-if="user" @click="signOut">Sign Out</button>
      </div>
    </div>

    <!-- Search and Filter Controls -->
    <div class="search-filters">
      <input v-model="search.name" placeholder="Search by Factory Name">
      <input v-model="search.chocolateName" placeholder="Search by Chocolate Name">
      <input v-model="search.location" placeholder="Search by Location">
      <input v-model="search.averageRating" type="number" min="0" max="5" step="0.1" placeholder="Rating">
      
      <select v-model="sortCriteria">
        <option value="name">Name</option>
        <option value="location">Location</option>
        <option value="averageRating">Average Rating</option>
      </select>
      <select v-model="sortOrder">
        <option value="asc">Ascending</option>
        <option value="desc">Descending</option>
      </select>
      
      <input type="checkbox" v-model="filters.onlyOpenFactories"> Show only open factories
      
      <select v-model="filters.chocolateKind">
        <option value="">All Kinds</option>
        <option value="Dark">Dark</option>
        <option value="Milk">Milk</option>
        <option value="White">White</option>
      </select>
      
      <select v-model="filters.chocolateType">
        <option value="">All Types</option>
        <option value="Bar">Bar</option>
        <option value="Box">Box</option>
        <option value="Truffle">Truffle</option>
      </select>
    </div>

    <div v-for="factory in filteredSortedFactories" :key="factory.id" class="factory-card" @click="setSelectedFactory(factory.id)">
      <div class="factory-card-header">
        <h2>{{ factory.name }}</h2>
        <img :src="getLogoPath(factory.logo)" alt="Logo" class="factory-logo">
      </div>
      <p><strong>Location:</strong> {{ factory.location.street }}, {{ factory.location.city }}, {{ factory.location.state }}</p>
      <p><strong>Average Rating:</strong> {{ factory.averageRating }}</p>
      <p><strong>Working Hours:</strong> {{ factory.workingHours }}</p>
      <p><strong>Status:</strong> {{ factory.workStatus }}</p>
      <p><strong>Comments:</strong> {{ factory.comments }}</p>
      <div class="factory-map" :id="'map-' + factory.id"></div>
      <div class="factory-card-buttons">
        <button @click.stop="viewMore(factory.id)" class="btn-primary">View More</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { fromLonLat } from 'ol/proj';

export default {
  props: ['username', 'factoryId'],
  data() {
    return {
      factories: [],
      user: null,
      selectedFactoryId: this.factoryId || null,
      search: {
        name: '',
        chocolateName: '',
        location: '',
        averageRating: ''
      },
      sortCriteria: 'name',
      sortOrder: 'asc',
      filters: {
        onlyOpenFactories: false,
        chocolateKind: '',
        chocolateType: ''
      }
    };
  },
  mounted() {
    this.fetchUser();
    this.fetchFactories();
  },
  methods: {
    fetchUser() {
      axios.get(`http://localhost:8080/WebShopAppREST/rest/users`)
        .then(response => {
          this.user = response.data.find(user => user.username === this.username);
        })
        .catch(error => {
          console.error('Error fetching user details', error);
        });
    },
    fetchFactories() {
      axios.get('http://localhost:8080/WebShopAppREST/rest/factories')
        .then(response => {
          this.factories = response.data;
          this.$nextTick(() => {
            this.factories.forEach(factory => {
              this.initMap(factory);
            });
          });
        })
        .catch(error => {
          console.error('Error fetching factories', error);
        });
    },
    initMap(factory) {
      const map = new Map({
        target: 'map-' + factory.id,
        layers: [
          new TileLayer({
            source: new OSM(),
          }),
        ],
        view: new View({
          center: fromLonLat([factory.location.longitude, factory.location.latitude]),
          zoom: 12, // Podesiti zumiranje da se vidi grad
        }),
      });
    },
    viewMore(factoryId) {
      this.$router.push({
        name: 'DetailsFactoryManager',
        params: { id: factoryId, username: this.user.username }
      });
    },
    addChocolate(factoryId) {
      this.$router.push({ name: 'AddChocolate', params: { id: factoryId, username: this.username } });
    },
    getLogoPath(logo) {
      try {
        return require(`../assets/${logo}`);
      } catch (e) {
        console.error('Error loading logo image', e);
        return '';  
      }
    },
    signOut() {
      this.$router.push({ name: 'FactoryList' });
    },
    setSelectedFactory(factoryId) {
      this.selectedFactoryId = factoryId;
    }
  },
  computed: {
    filteredSortedFactories() {
      let result = this.factories;

      // Apply search filters
      if (this.search.name) {
        result = result.filter(factory => factory.name.toLowerCase().includes(this.search.name.toLowerCase()));
      }
      if (this.search.chocolateName) {
        result = result.filter(factory => 
          factory.chocolates.some(chocolate => chocolate.name.toLowerCase().includes(this.search.chocolateName.toLowerCase()))
        );
      }
      if (this.search.location) {
        result = result.filter(factory => 
          factory.location.city.toLowerCase().includes(this.search.location.toLowerCase()) ||
          factory.location.state.toLowerCase().includes(this.search.location.toLowerCase())
        );
      }
      if (this.search.averageRating) {
        result = result.filter(factory => factory.averageRating >= parseFloat(this.search.averageRating));
      }

      // Apply filters
      if (this.filters.onlyOpenFactories) {
        result = result.filter(factory => factory.workStatus === 'Working');
      }
      if (this.filters.chocolateKind) {
        result = result.filter(factory => 
          factory.chocolates.some(chocolate => chocolate.chocolateKind === this.filters.chocolateKind)
        );
      }
      if (this.filters.chocolateType) {
        result = result.filter(factory => 
          factory.chocolates.some(chocolate => chocolate.chocolateType === this.filters.chocolateType)
        );
      }

      // Apply sorting
      result = result.slice().sort((a, b) => {
        let sortVal = 0;
        if (this.sortCriteria === 'name') {
          sortVal = a.name.localeCompare(b.name);
        } else if (this.sortCriteria === 'location') {
          sortVal = a.location.city.localeCompare(b.location.city) || a.location.state.localeCompare(b.location.state);
        } else if (this.sortCriteria === 'averageRating') {
          sortVal = a.averageRating - b.averageRating;
        }

        if (this.sortOrder === 'desc') {
          sortVal *= -1;
        }
        return sortVal;
      });

      return result;
    }
  }
};
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&display=swap');

.container {
  font-family: 'Open Sans', sans-serif;
  background: #f9f9f9;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.main-title {
  font-size: 2.5rem;
  color: #333;
}

.auth {
  display: flex;
  align-items: center;
}

.auth span {
  margin-right: 20px;
  font-size: 1rem;
  color: #333;
}

.auth button {
  background-color: #6b3e26;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.auth button:hover {
  background-color: #552e1a;
}

.factory-card {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  cursor: pointer;
}

.factory-card:hover {
  background-color: #f1f1f1;
}

.factory-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.factory-logo {
  width: 100px;
  height: auto;
}

.factory-card-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.btn-primary, .btn-secondary {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
}

.btn-primary {
  background-color: #6b3e26;
  color: white;
}

.btn-secondary {
  background-color: #ddd;
  color: #333;
}

.btn-primary:hover {
  background-color: #552e1a;
}

.btn-secondary:hover {
  background-color: #ccc;
}

.factory-map {
  width: 100%;
  height: 300px; /* Povećana visina za bolji pregled */
  margin-top: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}
</style>
