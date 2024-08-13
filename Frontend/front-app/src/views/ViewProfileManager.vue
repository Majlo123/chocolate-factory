<template>
    <div v-if="user" class="container">
      <header class="header">
        <h1 class="main-title">User information</h1>
        <router-link 
          v-if="user"
          :to="{ name: 'FactoryListManager', params: { username: user.username, factoryId: selectedFactoryId } }">Home</router-link>
        <router-link 
          v-if="user"
          :to="{ name: 'FactoryDetailsManager', params: { username: user.username, factoryId: selectedFactoryId } }">
          Edit factory
        </router-link>
        <router-link 
          v-if="user"
          :to="{ name: 'ViewProfileManager', params: { username: user.username, factoryId: selectedFactoryId } }">View Profile</router-link>
        <div class="auth">
          <span v-if="user">Welcome, {{ user.firstName }} {{ user.lastName }}!</span>
          <router-link v-else to="/sign-in">Sign In</router-link>
          <button v-if="user" @click="signOut">Sign Out</button>
        </div>
      </header>
      <div class="factory-info">
        <p><strong>Username:</strong> {{ user.username }}</p>
        <p>
          <strong>Password:</strong>
          <span v-if="!user.editingPassword">
            <input :type="passwordFieldType" :value="user.password" readonly class="password-input">
            <button @click="togglePasswordVisibility">{{ passwordFieldType === 'password' ? 'Show' : 'Hide' }}</button>
          </span>
          <span v-else>
            <input :type="newPasswordFieldType"  v-model="user.newPassword" class="password-input">
            <button @click="toggleNewPasswordVisibility">{{ newPasswordFieldType === 'password' ? 'Show' : 'Hide' }}</button>
            <button @click="updatePassword(user)">Save</button>
          </span>
          <button @click="editPassword(user)" class="edit-button">
            <img src="../assets/edit.png" alt="Edit" class="edit-icon">
          </button>
        </p>
        <p>
          <strong>First name:</strong>
          <span v-if="!user.editingFirstName">{{ user.firstName }}</span>
          <input v-else type="input" v-model="user.newFirstName" @keyup.enter="updateFirstName(user)" class="firstName-input">
          <button @click="editFirstName(user)" class="edit-button">
            <img src="../assets/edit.png" alt="Edit" class="edit-icon">
          </button>
        </p>
        <p>
          <strong>Last name:</strong>
          <span v-if="!user.editingLastName">{{ user.lastName }}</span>
          <input v-else type="input" v-model="user.newLastName" @keyup.enter="updateLastName(user)" class="lastName-input">
          <button @click="editLastName(user)" class="edit-button">
            <img src="../assets/edit.png" alt="Edit" class="edit-icon">
          </button>
        </p>
        <p>
      <strong>Gender:</strong>
      <span v-if="!user.editingGender">{{ user.gender === 'M' ? 'Male' : 'Female' }}</span>
      <select v-else v-model="user.newGender" @change="updateGender(user)" required class="form-control">
        <option value="M">Male</option>
        <option value="F">Female</option>
      </select>
      <button @click="editGender(user)" class="edit-button">
        <img src="../assets/edit.png" alt="Edit" class="edit-icon">
      </button>
    </p>
    <p>
          <strong>Date of birth:</strong>
          <span v-if="!user.editingBirthDate">{{ user.birthDate }}</span>
          <input v-else type="date" v-model="user.newBirthDate" @keyup.enter="updateBirthDate(user)" class="birthDate-input">
          <button @click="editBirthDate(user)" class="edit-button">
            <img src="../assets/edit.png" alt="Edit" class="edit-icon">
          </button>
        </p>
        <p><strong>Role:</strong> {{ user.role }}</p>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    props: ['username'],
    data() {
      return {
        user: null,
        passwordFieldType: 'password',
        newPasswordFieldType: '',
        editingPassword: false,
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
      editPassword(user) {
        user.editingPassword = true;
        user.newPassword = user.password; 
      },
      updatePassword(user) {
        axios.put(`http://localhost:8080/WebShopAppREST/rest/users/${this.user.username}/changePassword`, {
          password: user.newPassword
        })
        .then(response => {
          user.password = user.newPassword;
          user.editingPassword = false;
        })
        .catch(error => {
          console.error('Error updating password', error);
        });
      },
      editFirstName(user) {
        user.editingFirstName = true;
        user.newFirstName = user.firstName;
      },
      updateFirstName(user) {
        axios.put(`http://localhost:8080/WebShopAppREST/rest/users/${this.user.username}/changeFirstName`, {
          firstName: user.newFirstName
        })
        .then(response => {
          user.firstName = user.newFirstName;
          user.editingFirstName = false;
        })
        .catch(error => {
          console.error('Error updating first name', error);
        });
      },
      editLastName(user) {
        user.editingLastName = true;
        user.newLastName = user.lastName;
      },
      updateLastName(user) {
        axios.put(`http://localhost:8080/WebShopAppREST/rest/users/${this.user.username}/changeLastName`, {
          lastName: user.newLastName
        })
        .then(response => {
          user.lastName = user.newLastName;
          user.editingLastName = false;
        })
        .catch(error => {
          console.error('Error updating last name', error);
        });
      },
      editGender(user) {
        user.editingGender = true;
        user.newGender = user.gender;
      },
      updateGender(user) {
        axios.put(`http://localhost:8080/WebShopAppREST/rest/users/${this.user.username}/changeGender`, {
          gender: user.newGender
        })
        .then(response => {
          user.gender = user.newGender;
          user.editingGender = false;
        })
        .catch(error => {
          console.error('Error updating gender', error);
        });
      },
      editBirthDate(user) {
        user.editingBirthDate = true;
        user.newBirthDate = user.birthDate;
      },
      updateBirthDate(user) {
        axios.put(`http://localhost:8080/WebShopAppREST/rest/users/${this.user.username}/changeBirthDate`, {
          birthDate: user.newBirthDate
        })
        .then(response => {
          user.birthDate = user.newBirthDate;
          user.editingBirthDate = false;
        })
        .catch(error => {
          console.error('Error updating birth date', error);
        });
      },
      togglePasswordVisibility() {
        this.passwordFieldType = this.passwordFieldType === 'password' ? 'text' : 'password';
      },
      toggleNewPasswordVisibility() {
        this.newPasswordFieldType = this.newPasswordFieldType === 'password' ? 'text' : 'password';
      },
      signOut() {
        this.$router.push({ name: 'FactoryList' });
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
  
  .password-input {
    border: none;
    background: none;
    outline: none;
    font-family: inherit;
    font-size: inherit;
  }
  
  .toggle-password {
    cursor: pointer;
    margin-left: 10px;
    font-size: 1.2em;
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
    background-color: darkviolet;
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
  