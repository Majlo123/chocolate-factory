<template>
    <div class="container">
      <button @click="goBackToFactoryDetails" class="btn-back">Back to Factories</button>
      <h1 class="main-title">Add Chocolate to {{ factoryName }}</h1>
      <form @submit.prevent="addChocolate" class="chocolate-form">
        <div class="form-group">
          <label>Name:</label>
          <input type="text" v-model="newChocolate.name" required class="form-control">
        </div>
        <div class="form-group">
          <label>Price:</label>
          <input type="number" v-model="newChocolate.price" required class="form-control">
        </div>
        <div class="form-group">
          <label>Chocolate Kind:</label>
          <select v-model="newChocolate.chocolateKind" class="form-control">
            <option value="White">White</option>
            <option value="Dark">Dark</option>
            <option value="Milk">Milk</option>
          </select>
        </div>
        <div class="form-group">
          <label>Chocolate Type:</label>
          <input type="text" v-model="newChocolate.chocolateType" required class="form-control">
        </div>
        <div class="form-group">
          <label>Weight:</label>
          <input type="number" v-model="newChocolate.weight" required class="form-control">
        </div>
        <div class="form-group">
          <label>Description:</label>
          <textarea v-model="newChocolate.description" required class="form-control"></textarea>
        </div>
        <div class="form-group">
          <label>Image:</label>
          <input type="file" @change="onFileChange" class="form-control">
          <img v-if="previewImage" :src="previewImage" class="preview-image" />
        </div>
        <div class="form-buttons">
          <button type="submit" class="btn-primary">Add Chocolate</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    props: ['factoryId', 'username'],
    data() {
      return {
        factoryName: '',
        newChocolate: {
          name: '',
          price: 0,
          chocolateKind: 'White',
          chocolateType: '',
          weight: 0,
          description: '',
          image: '',
          quantity: 0
        },
        previewImage: null
      };
    },
    mounted() {
      axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${this.factoryId}`)
        .then(response => {
          this.factoryName = response.data.name;
        })
        .catch(error => {
          console.error("Error fetching factory details", error);
        });
    },
    methods: {
      onFileChange(event) {
        const file = event.target.files[0];
        if (file) {
          this.newChocolate.image = file.name;
          this.previewImage = URL.createObjectURL(file);
        }
      },
      addChocolate() {
        axios.post('http://localhost:8080/WebShopAppREST/rest/chocolates', this.newChocolate)
          .then(response => {
            const chocolateId = response.data.id;
            axios.put(`http://localhost:8080/WebShopAppREST/rest/factories/${this.factoryId}/addChocolate/${chocolateId}`)
              .then(() => {
                alert('Chocolate added successfully');
                this.$router.push({ name: 'FactoryDetailsManager', params: { username: this.username, factoryId: this.factoryId } });
              })
              .catch(error => {
                console.error("Error adding chocolate to factory", error);
              });
          })
          .catch(error => {
            console.error("Error adding chocolate", error);
          });
      },
      goBackToFactoryDetails() {
        this.$router.push({ name: 'FactoryDetailsManager', params: { username: this.username, factoryId: this.factoryId } });
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
  
  .btn-back {
    background-color: #6b3e26;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1rem;
  }
  
  .btn-back:hover {
    background-color: #552e1a;
  }
  
  .main-title {
    text-align: center;
    color: #6b3e26;
    margin-bottom: 20px;
  }
  
  .chocolate-form {
    background: #fff;
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  .form-control {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  .preview-image {
    width: 100px;
    height: auto;
    margin-top: 10px;
  }
  
  .form-buttons {
    text-align: center;
  }
  
  .btn-primary {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1rem;
    background-color: #6b3e26;
    color: white;
  }
  
  .btn-primary:hover {
    background-color: #552e1a;
  }
  </style>
  