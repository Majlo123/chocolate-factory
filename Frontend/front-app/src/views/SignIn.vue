<template>
  <div class="sign-in">
    <h2>Sign In</h2>
    <form @submit.prevent="signIn">
      <div>
        <label for="username">Username:</label>
        <input type="text" id="username" v-model="username" required>
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required>
      </div>
      <br>
      <router-link to="/sign-up">Don't have account? Sign up</router-link>
      <br>
      <button type="submit">Sign In</button>
    </form>
    <p v-if="errorMessage">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
  signIn() {
    axios.get('http://localhost:8080/WebShopAppREST/rest/users')
      .then(response => {
        const user = response.data.find(user => user.username === this.username && user.password === this.password);
        if (user) {
          if(user.blocked==true){
            this.errorMessage = 'Cant log in. Blocked by administrator.';
          }
          else{
          if (user.role === 'Menadžer') { 
            this.$router.push({ name: 'FactoryListManager', params: { username: user.username, factoryId: user.factory.id } });
          }
          else if(user.role === 'Kupac'){
            this.$router.push({ name: 'FactoryListBuyer', params: { username: user.username } });
          }
          else if (user.role === 'Radnik') { 
            this.$router.push({ name: 'FactoryListWorker', params: { username: user.username, factoryId: user.factory.id } });
          }
          else if (user.role === 'Administrator') { 
            this.$router.push({ name: 'FactoryListAdministrator', params: { username: user.username} });
          }
        }
        } else {
          this.errorMessage = 'Invalid credentials. Please try again.';
        }
      })
      .catch(error => {
        console.error('Error during sign-in:', error);
        this.errorMessage = 'An error occurred. Please try again later.';
      });
  }
}
};
</script>

<style scoped>
.sign-in {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
.sign-in h2 {
  text-align: center;
  margin-bottom: 20px;
}
.sign-in form {
  display: flex;
  flex-direction: column;
}
.sign-in label {
  margin-bottom: 5px;
}
.sign-in input {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.sign-in button {
  padding: 10px;
  background-color: #6b3e26;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.sign-in button:hover {
  background-color: #552e1a;
}
.sign-in p {
  color: red;
  text-align: center;
}
</style>
