import { createRouter, createWebHashHistory } from 'vue-router';
import FactoryList from '../views/FactoryList.vue';
import FactoryDetails from '../views/FactoryDetails.vue';
import AddChocolate from '../views/AddChocolate.vue';
import ChocolateEditComponent from '../views/ChocolateEditComponent.vue';
import SignIn from '../views/SignIn.vue';
import SignUp from '../views/SignUp.vue';
import SignUpWorker from '../views/SignUpWorker.vue';
import FactoryListManager from '../views/FactoryListManager.vue';
import FactoryListBuyer from '../views/FactoryListBuyer.vue';
import FactoryListWorker from '../views/FactoryListWorker.vue';
import FactoryDetailsManager from '../views/FactoryDetailsManager.vue';
import FactoryDetailsWorker from '../views/FactoryDetailsWorker.vue';
const routes = [
  {
    path: '/',
    name: 'FactoryList',
    component: FactoryList
  },
  {
    path: '/sign-in',
    name: 'SignIn',
    component: SignIn
  },
  {
    path: '/sign-up',
    name: 'SignUp',
    component: SignUp
  },
  {
    path: '/sign-up-worker/:factoryId',
    name: 'SignUpWorker',
    component: SignUpWorker
  },
  {
    path: '/factories/:id',
    name: 'FactoryDetails',
    component: FactoryDetails
  },
  {
    path: '/factories/:factoryId/add/:username',
    name: 'AddChocolate',
    component: AddChocolate,
    props: true
  },
  {
    path: '/factories/:factoryId/users/:username/chocolates/:chocolateId/edit',
    name: 'EditChocolate',
    component: ChocolateEditComponent,
    props: true
  },
  {
    path: '/factory-list-manager/:username/factories/:factoryId',
    name: 'FactoryListManager',
    component: FactoryListManager,
    props: true
  },
  {
    path: '/factory-list-worker/:username/factories/:factoryId',
    name: 'FactoryListWorker',
    component: FactoryListWorker,
    props: true
  },
  {
    path: '/factory-list-buyer/:username',
    name: 'FactoryListBuyer',
    component: FactoryListBuyer,
    props: true
  },
  {
    path: '/factory-details-manager/:username/factories/:factoryId',
    name: 'FactoryDetailsManager',
    component: FactoryDetailsManager,
    props: true
  },
  {
    path: '/factory-details-worker/:username/factories/:factoryId',
    name: 'FactoryDetailsWorker',
    component: FactoryDetailsWorker,
    props: true
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router;
