<template>
  <div class="container">
    <h1 class="main-title">Add worker</h1>
    <form @submit.prevent="signUp" class="signup-form">
      <div class="form-group">
        <label>Username:</label>
        <input type="text" v-model="newUser.username" required class="form-control">
      </div>
      <div class="form-group">
        <label>Password:</label>
        <input type="password" v-model="newUser.password" required class="form-control">
      </div>
      <div class="form-group">
        <label>Confirm Password:</label>
        <input type="password" v-model="confirmPassword" required class="form-control">
      </div>
      <div class="form-group">
        <label>First Name:</label>
        <input type="text" v-model="newUser.firstName" required class="form-control">
      </div>
      <div class="form-group">
        <label>Last Name:</label>
        <input type="text" v-model="newUser.lastName" required class="form-control">
      </div>
      <div class="form-group">
        <label>Gender:</label>
        <select v-model="newUser.gender" required class="form-control">
          <option value="Muški">Male</option>
          <option value="Ženski">Female</option>
        </select>
      </div>
      <div class="form-group">
        <label>Date of Birth:</label>
        <input type="date" v-model="newUser.birthDate" required class="form-control">
      </div>
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      <div class="form-buttons">
        <button type="submit" class="btn-primary">Register</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newUser: {
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        gender: 'M',
        birthDate: '',
      },
      confirmPassword: '',
      errorMessage: '',
    };
  },
  methods: {
    async signUp() {
      this.errorMessage = '';

      if (this.newUser.password !== this.confirmPassword) {
        this.errorMessage = 'Passwords do not match.';
        return;
      }

      try {
        const response = await axios.post(
          `http://localhost:8080/WebShopAppREST/rest/users/signupmanager/`,
          {
            ...this.newUser,
            birthDate: new Date(this.newUser.birthDate).toISOString().split('T')[0],
          }
        );
        this.$store.commit('setManager', this.newUser.username);
        alert('Registration successful!');
        this.$router.go(-1);
      } catch (error) {
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data;
        } else {
          this.errorMessage = 'Error during registration.';
        }
      }
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

.main-title {
  text-align: center;
  color: #6b3e26;
  margin-bottom: 20px;
}

.signup-form {
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

.error-message {
  color: red;
  margin-bottom: 15px;
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
