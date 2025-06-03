// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import AccountForm from './components/AccountForm.vue'
import RegisterVehicle from "./components/RegisterVehicle.vue";

const routes = [
    {
        path: '/create-account',
        name: 'CreateAccount',
        component: AccountForm
    },
    {
        path: '/register-vehicle',
        name: 'RegisterVehicle',
        component: RegisterVehicle
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
