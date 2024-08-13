import { createRouter, createWebHashHistory } from 'vue-router';
import FactoryList from '../views/FactoryList.vue';
import UserList from '../views/UserList.vue';
import FactoryDetails from '../views/FactoryDetails.vue';
import DetailsFactoryAdministrator from '../views/DetailsFactoryAdministrator.vue';
import DetailsFactoryManager from '../views/DetailsFactoryManager.vue';
import DetailsFactoryWorker from '../views/DetailsFactoryWorker.vue';
import DetailsFactoryBuyer from '../views/DetailsFactoryBuyer.vue';
import AddChocolate from '../views/AddChocolate.vue';
import AddFactory from '../views/AddFactory.vue';
import ChocolateEditComponent from '../views/ChocolateEditComponent.vue';
import SignIn from '../views/SignIn.vue';
import SignUp from '../views/SignUp.vue';
import SignUpWorker from '../views/SignUpWorker.vue';
import SignUpManager from '../views/SignUpManager.vue';
import FactoryListManager from '../views/FactoryListManager.vue';
import FactoryListBuyer from '../views/FactoryListBuyer.vue';
import FactoryListWorker from '../views/FactoryListWorker.vue';
import FactoryListAdministrator from '../views/FactoryListAdministrator.vue';
import FactoryDetailsManager from '../views/FactoryDetailsManager.vue';
import FactoryDetailsWorker from '../views/FactoryDetailsWorker.vue';
import ViewProfileManager from '../views/ViewProfileManager.vue';
import ViewProfileWorker from '../views/ViewProfileWorker.vue';
import ViewProfileAdministrator from '../views/ViewProfileAdministrator.vue';

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
    path: '/sign-up-manager/',
    name: 'SignUpManager',
    component: SignUpManager
  },
  {
    path: '/factories/:id/users/:username',
    name: 'FactoryDetails',
    component: FactoryDetails
  },
  {
    path: '/user-list/:username',
    name: 'UserList',
    component: UserList
  },
  {
    path: '/factories/:id/users/:username',
    name: 'DetailsFactoryAdministrator',
    component: DetailsFactoryAdministrator
  },
  {
    path: '/factories/:id/users/:username',
    name: 'DetailsFactoryBuyer',
    component: DetailsFactoryBuyer
  },
  {
    path: '/factories/:id/users/:username',
    name: 'DetailsFactoryManager',
    component: DetailsFactoryManager
  },
  {
    path: '/factories/:id/users/:username',
    name: 'DetailsFactoryBuyer',
    component: DetailsFactoryBuyer
  },
  {
    path: '/factories/:id/users/:username',
    name: 'DetailsFactoryWorker',
    component: DetailsFactoryWorker
  },
  {
    path: '/factories/:factoryId/add/:username',
    name: 'AddChocolate',
    component: AddChocolate,
    props: true
  },
  {
    path: '/add/:username',
    name: 'AddFactory',
    component: AddFactory,
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
    path: '/factory-list-administrator/:username',
    name: 'FactoryListAdministrator',
    component: FactoryListAdministrator,
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
  },
  {
    path: '/view-profile-manager/:username/factories/:factoryId',
    name: 'ViewProfileManager',
    component: ViewProfileManager,
    props: true
  },
  {
    path: '/view-profile-worker/:username/factories/:factoryId',
    name: 'ViewProfileWorker',
    component: ViewProfileWorker,
    props: true
  },
  {
    path: '/view-profile-administrator/:username',
    name: 'ViewProfileAdministrator',
    component: ViewProfileAdministrator,
    props: true
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router;
