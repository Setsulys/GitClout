/*
import { createApp } from 'vue'
import App from './App.vue'
import 'semantic-ui-css/semantic.min.css';
<<<<<<< HEAD
import { createRouter, createWebHistory } from 'vue-router'
=======
//import Vue from 'vue';
//import SuiVue from 'semantic-ui-vue';
//import '../semantic/dist/semantic.min.css';
*/
import { createApp } from 'vue';
import App from './App.vue';
import 'semantic-ui-css/semantic.min.css';
import { createRouter, createWebHistory } from 'vue-router';

import HomePage from "@/components/HomePage.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'HomePage',
            component: HomePage,
        },
    ],

});

const app = createApp(App);

// Utilisez le routeur dans votre application
app.use(router);

app.mount('#app');
