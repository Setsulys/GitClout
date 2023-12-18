// Import your Vue component
import NewTabComponent from './HelloWorld.vue';

// Create a new Vue instance with the component
const app = new Vue({
    render: (h) => h(NewTabComponent),
});

// Mount the app to the element with id 'app' in the new tab
app.$mount('#app');