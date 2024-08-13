<template>
    <div v-if="factory" class="container">
      <button @click="navigateToFactoryList" class="btn-back">Back to Factories</button>
      <h1 class="factory-title">{{ factory.name }}</h1>
      <img :src="getLogoPath(factory.logo)" alt="Logo" class="factory-logo-large">
      <div class="factory-info">
        <p><strong>Location:</strong> {{ factory.location.street }}, {{ factory.location.city }}, {{ factory.location.state }}</p>
        <p><strong>Average Rating:</strong> {{ factory.averageRating }}</p>
        <p><strong>Working Hours:</strong> {{ factory.workingHours }}</p>
        <p><strong>Status:</strong> {{ factory.workStatus }}</p>
      </div>
      <h2 class="chocolate-section-title">Chocolates</h2>
      <div v-for="chocolate in factory.chocolates" :key="chocolate.id" class="chocolate-card">
        <p><strong>Name:</strong> {{ chocolate.name }}</p>
        <img :src="getLogoPath(chocolate.image)" alt="Chocolate-Logo" class="chocolate-logo">
        <p><strong>Price:</strong> {{ chocolate.price }} RSD</p>
        <p><strong>Type:</strong> {{ chocolate.chocolateKind }} - {{ chocolate.chocolateType }}</p>
        <p><strong>Weight:</strong> {{ chocolate.weight }} g</p>
        <p><strong>Description:</strong> {{ chocolate.description }}</p>
        <p><strong>Quantity:</strong> {{ chocolate.quantity }}</p>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        factory: null
      };
    },
    methods: {
      fetchFactory() {
        const id = this.$route.params.id;
        axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${id}`)
          .then(response => {
            console.log('Factory data:', response.data); // Debug output
            this.factory = response.data;
          })
          .catch(error => {
            console.error('Error fetching factory details', error);
          });
      },
      navigateToFactoryList() {
        this.$router.push({ name: 'FactoryListManager', params: {username:this.$route.params.username,factoryId: this.$route.params.id,} });
      },
      getLogoPath(logo) {
        try {
          return require(`../assets/${logo}`);
        } catch (e) {
          console.error("Error loading logo image", e);
          return ''; 
        }
      },
    },
    mounted() {
      this.fetchFactory();
    }
  }
  </script>
  
  <style scoped>
  @import url('https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&display=swap');
  
  .container {
    font-family: 'Open Sans', sans-serif;
    background: #f9f9f9;
    padding: 20px;
  }
  
  .btn-back {
    margin-bottom: 20px;
    padding: 10px 20px;
    background-color: #ddd;
    color: #333;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .btn-back:hover {
    background-color: #ccc;
  }
  
  .factory-title {
    text-align: center;
    font-size: 2rem;
    color: #333;
    margin-bottom: 20px;
  }
  
  .factory-logo-large {
    display: block;
    margin: 0 auto 20px;
    width: 150px;
    height: auto;
  }
  
  .factory-info {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .chocolate-section-title {
    text-align: center;
    font-size: 1.8rem;
    color: #333;
    margin-bottom: 20px;
  }
  
  .chocolate-card {
    background: #fff;
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s;
  }
  
  .chocolate-card:hover {
    transform: scale(1.02);
  }
  
  .chocolate-logo {
    width: 100px;
    height: auto;
    display: block;
    margin: 0 auto 10px;
  }
  
  .chocolate-card-buttons {
    display: flex;
    justify-content: space-between;
    margin-top: 10px;
  }
  
  .btn-primary, .btn-danger {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1rem;
  }
  
  .btn-primary {
    background-color: #6b3e26;
    color: white;
    margin-right: 10px;
  }
  
  .btn-danger {
    background-color: #e74c3c;
    color: white;
  }
  
  .btn-primary:hover {
    background-color: #552e1a;
  }
  
  .btn-danger:hover {
    background-color: #c0392b;
  }
  </style>
  