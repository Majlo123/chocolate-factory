import { createStore } from 'vuex';

const store = createStore({
  state: {
    factoryForm: {
      name: '',
      location: {
        street: '',
        city: '',
        state: '',
        latitude: 0.0,
        longitude: 0.0,
      },
      workingHours: '',
      workStatus: 'Working',
      logo: '',
      selectedManager: null,
    },
  },
  mutations: {
    setFactoryForm(state, form) {
      state.factoryForm = form;
    },
    setManager(state, manager) {
      state.factoryForm.selectedManager = manager;
    },
    resetFactoryForm(state) {
      state.factoryForm = {
        name: '',
        location: {
          street: '',
          city: '',
          state: '',
          latitude: 0.0,
          longitude: 0.0,
        },
        workingHours: '',
        workStatus: 'Working',
        logo: '',
        selectedManager: null,
      };
    },
  },
});

export default store;
