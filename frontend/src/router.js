// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import AccountForm from './components/AccountForm.vue'
import RegisterVehicle from "./components/RegisterVehicle.vue";
import VehicleRegistrationStatistics from "./components/VehicleRegistrationStatistics.vue";

const routes = [
    {
        path: '/',
        redirect: '/create-account',
    },
    {
        path: '/create-account',
        name: 'CreateAccount',
        component: AccountForm
    },
    {
        path: '/register-vehicle',
        name: 'RegisterVehicle',
        component: RegisterVehicle
    },
    {
        path: '/statistics',
        name: 'VehicleRegistrationStatistics',
        component: VehicleRegistrationStatistics
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
