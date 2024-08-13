<template>
    <div v-if="chocolate" class="container">
        <h2>Edit Chocolate</h2>
        <form @submit.prevent="updateChocolate" class="chocolate-form">
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" v-model="chocolate.name" id="name" required class="form-control" />
            </div>
            <div class="form-group">
                <label for="price">Price:</label>
                <input type="number" v-model="chocolate.price" id="price" required class="form-control" />
            </div>
            <div class="form-group">
                <label for="chocolateKind">Kind:</label>
                <input type="text" v-model="chocolate.chocolateKind" id="chocolateKind" required class="form-control" />
            </div>
            <div class="form-group">
                <label for="chocolateType">Type:</label>
                <input type="text" v-model="chocolate.chocolateType" id="chocolateType" required class="form-control" />
            </div>
            <div class="form-group">
                <label for="weight">Weight:</label>
                <input type="number" v-model="chocolate.weight" id="weight" required class="form-control" />
            </div>
            <div class="form-group">
                <label for="description">Description:</label>
                <textarea v-model="chocolate.description" id="description" required class="form-control"></textarea>
            </div>
            <div class="form-group">
                <label for="image">Image:</label>
                <input type="file" @change="onFileChange" id="image" class="form-control" />
                <p v-if="chocolate.image"></p>
                <img v-if="previewImage || chocolate.image" :src="previewImage || getImagePath(chocolate.image)" class="preview-image" />
            </div>
            <div class="form-buttons">
                <button type="submit" class="btn-primary">Save</button>
                <button @click="cancelEdit" class="btn-secondary">Cancel</button>
            </div>
        </form>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    props: ['factoryId', 'chocolateId'],
    data() {
        return {
            chocolate: null,
            previewImage: null
        };
    },
    methods: {
        fetchChocolate() {
            axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${this.factoryId}/chocolates/${this.chocolateId}`)
                .then(response => {
                    this.chocolate = response.data;
                })
                .catch(error => {
                    console.error('Error fetching chocolate details', error);
                });
        },
        onFileChange(event) {
            const file = event.target.files[0];
            if (file) {
                this.chocolate.image = file.name; 
                this.previewImage = URL.createObjectURL(file); 
            }
        },
        getImagePath(imageName) {
            return require(`../assets/${imageName}`);
        },
        updateChocolate() {
            axios.put(`http://localhost:8080/WebShopAppREST/rest/factories/${this.factoryId}/chocolates/${this.chocolateId}`, this.chocolate)
                .then(() => {
                    this.$router.push({ name: 'FactoryDetailsManager', params: { id: this.factoryId,username:this.$route.params.username} });
                })
                .catch(error => {
                    console.error('Error updating chocolate', error);
                });
        },
        cancelEdit() {
            this.$router.push({ name: 'FactoryDetails', params: { id: this.factoryId } });
        }
    },
    mounted() {
        this.fetchChocolate();
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
    display: flex;
    justify-content: space-between;
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
    margin-right: 10px;
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
</style>
