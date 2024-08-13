<template>
  <div v-if="factory" class="container">
    <header class="header">
      <h1 class="main-title">Factory Details</h1>
      <router-link 
          v-if="user && selectedFactoryId" 
          :to="{ name: 'FactoryListWorker', params: { username: user.username, factoryId: selectedFactoryId } }">Home</router-link>
        <router-link 
          v-if="user && selectedFactoryId" 
          :to="{ name: 'FactoryDetailsWorker', params: { username: user.username, factoryId: selectedFactoryId } }">
          Edit factory
        </router-link>
        <router-link 
          v-if="user && selectedFactoryId" 
          :to="{ name: 'ViewProfileWorker', params: { username: user.username, factoryId: selectedFactoryId } }">View Profile</router-link>
      <div class="auth">
        <span v-if="user">Welcome, {{ user.firstName }} {{ user.lastName }}!</span>
        <button v-if="user" @click="signOut">Sign Out</button>
      </div>
    </header>
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
          <p>
              <strong>Quantity:</strong>
              <span v-if="!chocolate.editingQuantity">{{ chocolate.quantity }}</span>
              <input v-else type="number" v-model="chocolate.newQuantity" @keyup.enter="updateQuantity(chocolate)" class="quantity-input">
              <button @click="editQuantity(chocolate)" class="edit-button">
                  <img src="../assets/edit.png" alt="Edit" class="edit-icon">
              </button>
          </p>
      </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['username', 'factoryId'],
  data() {
    return {
      factory: null,
      user: null,
      selectedFactoryId: this.factoryId || null
    };
  },
methods: {
  fetchUser() {
    axios.get(`http://localhost:8080/WebShopAppREST/rest/users`)
      .then(response => {
        this.user = response.data.find(user => user.username === this.$route.params.username);
      })
      .catch(error => {
        console.error('Error fetching user details', error);
      });
  },
  fetchFactory() {
    const factoryId = this.$route.params.factoryId;
    axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${factoryId}`)
      .then(response => {
        this.factory = response.data;
      })
      .catch(error => {
        console.error('Error fetching factory details', error);
      });
  },
  getLogoPath(logo) {
    try {
      return require(`../assets/${logo}`);
    } catch (e) {
      console.error("Error loading logo image", e);
      return '';  
    }
  },
  signOut() {
    this.$router.push({ name: 'FactoryList' });
  },
  addChocolate(factoryId) {
    this.$router.push({ name: 'AddChocolate', params: { id: factoryId, username: this.$route.params.username } });
  },
  editQuantity(chocolate) {
    chocolate.editingQuantity = true;
    chocolate.newQuantity = chocolate.quantity;
  },
  updateQuantity(chocolate) {
    axios.put(`http://localhost:8080/WebShopAppREST/rest/factories/${this.factory.id}/chocolatequantity/${chocolate.id}`, {
      quantity: chocolate.newQuantity
    })
      .then(response => {
        chocolate.quantity = chocolate.newQuantity;
        chocolate.editingQuantity = false;
      })
      .catch(error => {
        console.error('Error updating chocolate quantity', error);
      });
  }
},
mounted() {
  this.fetchFactory();
  this.fetchUser();
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

.centered-button {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.btn-add-chocolate {
  padding: 15px 30px;
  background-color: blueviolet;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-add-chocolate:hover {
  background-color: blueviolet;
}
.edit-button {
  background: none;
  border: none;
  cursor: pointer;
  margin-left: 10px;
}

.edit-icon {
  width: 20px;
  height: auto;
}
</style>

