import { createApp } from 'vue'
import App from './App.vue'
import 'semantic-ui-css/semantic.min.css';
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: App },
        // Add more routes for additional components
    ]
});

createApp(App)
    .use(router)
    .mount('#app');