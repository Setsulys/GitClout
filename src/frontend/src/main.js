/*
import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')
import 'semantic-ui-css/semantic.min.css';
//import Vue from 'vue';
//import SuiVue from 'semantic-ui-vue';
//import '../semantic/dist/semantic.min.css';
*/
import { createApp } from 'vue';
import App from './App.vue';
import 'semantic-ui-css/semantic.min.css';
import { createRouter, createWebHistory } from 'vue-router';

// Importez vos composants ici
import HelloWorld from './components/HelloWorld.vue';
//import NewFile from "@/components/NewFile.vue";
import HomePage from "@/components/HomePage.vue";
//import {getUri} from "axios";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'HomePage',
            component: HomePage,
        },
        {
            path: '/HelloWorld',
            name: 'HelloWorld',
            component: HelloWorld,
        },
        {
            path: '/NewFile',
            name: 'NewFile',
            component: location.origin + '/components/NewFile.vue',
        },
    ],

});

const app = createApp(App);

// Utilisez le routeur dans votre application
app.use(router);

app.mount('#app');