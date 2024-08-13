<template>
  <div v-if="user" class="container">
    <header class="header">
      <h1 class="main-title">Users list</h1>
      <router-link 
        v-if="user" 
        :to="{ name: 'FactoryListAdministrator', params: { username: user.username, factoryId: selectedFactoryId } }">Home</router-link>
      <router-link 
        v-if="user" 
        :to="{ name: 'AddFactory', params: { username: user.username } }">Add factory</router-link>
      <router-link 
        v-if="user" 
        :to="{ name: 'ViewProfileAdministrator', params: { username: user.username } }">View Profile</router-link>
      <router-link 
        v-if="user" 
        :to="{ name: 'UserList', params: { username: user.username} }">Users</router-link>
      <div class="auth">
        <span v-if="user">Welcome, {{ user.firstName }} {{ user.lastName }}!</span>
        <router-link v-else to="/sign-in">Sign In</router-link>
        <button v-if="user" @click="signOut">Sign Out</button>
      </div>
    </header>
    <div class="filters">
      <input v-model="searchFirstName" placeholder="Search by first name" />
      <input v-model="searchLastName" placeholder="Search by last name" />
      <input v-model="searchUsername" placeholder="Search by username" />
      <select v-model="selectedRole">
        <option value="">All Roles</option>
        <option value="Kupac">Kupac</option>
        <option value="Menadžer">Menadžer</option>
        <option value="Radnik">Radnik</option>
      </select>
      <select v-model="selectedGender">
        <option value="">All Genders</option>
        <option value="Muški">Muški</option>
        <option value="Ženski">Ženski</option>
      </select>
      <button @click="toggleSort('firstName')">Sort by First Name</button>
      <button @click="toggleSort('lastName')">Sort by Last Name</button>
      <button @click="toggleSort('username')">Sort by Username</button>
    </div>
    <div v-for="user in filteredUsers" :key="user.username" class="user-card">
      <p><strong>Username:</strong> {{ user.username }}</p>
      <p><strong>Password:</strong> {{ user.password }}</p>
      <p><strong>First Name:</strong> {{ user.firstName }}</p>
      <p><strong>Last Name:</strong> {{ user.lastName }}</p>
      <p><strong>Gender:</strong> {{ user.gender }}</p>
      <p><strong>Date of Birth:</strong> {{ user.birthDate }}</p>
      <p><strong>Role:</strong> {{ user.role }}</p>
      <div v-if="user.role !== 'Administrator'">
        <p><strong>Status:</strong> {{ user.blocked ? 'Blocked' : 'Active' }}</p>
        <button @click="toggleBlock(user)">
          {{ user.blocked ? 'Unblock' : 'Block' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['username'],
  data() {
    return {
      users: [],
      user: null,
      searchFirstName: '',
      searchLastName: '',
      searchUsername: '',
      selectedRole: '',
      selectedGender: '',
      sortKey: '',
      sortOrder: 1,
    };
  },
  computed: {
    filteredUsers() {
      return this.users
        .filter(user => {
          return (
            (!this.searchFirstName || user.firstName.toLowerCase().includes(this.searchFirstName.toLowerCase())) &&
            (!this.searchLastName || user.lastName.toLowerCase().includes(this.searchLastName.toLowerCase())) &&
            (!this.searchUsername || user.username.toLowerCase().includes(this.searchUsername.toLowerCase())) &&
            (!this.selectedRole || user.role === this.selectedRole) &&
            (!this.selectedGender || user.gender === this.selectedGender)
          );
        })
        .sort((a, b) => {
          if (!this.sortKey) return 0;
          let result = a[this.sortKey] > b[this.sortKey] ? 1 : -1;
          return result * this.sortOrder;
        });
    },
  },
  methods: {
    fetchUser() {
      axios
        .get(`http://localhost:8080/WebShopAppREST/rest/users`)
        .then(response => {
          this.user = response.data.find(user => user.username === this.$route.params.username);
        })
        .catch(error => {
          console.error('Error fetching user details', error);
        });
    },
    fetchUsers() {
      axios
        .get('http://localhost:8080/WebShopAppREST/rest/users')
        .then(response => {
          console.log('Fetched users:', response.data);
          this.users = response.data;
        })
        .catch(error => {
          console.error('Error fetching users', error);
        });
    },
    signOut() {
      this.$router.push({ name: 'FactoryList' });
    },
    toggleSort(key) {
      if (this.sortKey === key) {
        this.sortOrder *= -1;
      } else {
        this.sortKey = key;
        this.sortOrder = 1;
      }
    },
    toggleBlock(user) {
      const newStatus = !user.blocked;
      axios
        .put(`http://localhost:8080/WebShopAppREST/rest/users/${user.username}/toggleBlock`, { ...user, blocked: newStatus })
        .then(response => {
          user.blocked = newStatus;
        })
        .catch(error => {
          console.error('Error updating user status', error);
        });
    },
  },
  mounted() {
    this.fetchUser();
    this.fetchUsers();
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

.filters {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.user-card {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.user-card button {
  background-color: #6b3e26;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.user-card button:hover {
  background-color: #552e1a;
}
</style>
