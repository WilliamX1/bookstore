import Vue from 'vue'
import Router from 'vue-router'
import Login from '../components/Login'
import Home from '../components/Home'
import ShoppingCart from '../components/ShoppingCart'
import Register from '../components/Register'
import BookDetails from '../components/BookDetails'
import Order from '../components/Order'
import Admin from '../components/Admin'
import HistoryOrders from '../components/HistoryOrders'
import Statistics from '../components/Statistics'
import ChatRoom from '../components/ChatRoom'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'Login',
            component: Login
        },
        {
            path: '/Login',
            name: 'Login',
            component: Login
        },
        {
            path: '/Home',
            name: 'Home',
            component: Home
        },
        {
            path: '/ShoppingCart',
            name: 'ShoppingCart',
            component: ShoppingCart
        },
        {
            path: '/Register',
            name: 'Register',
            component: Register
        },
        {
            path: '/BookDetails',
            name: 'BookDetails',
            component: BookDetails
        },
        {
            path: '/Order',
            name: 'Order',
            component: Order
        },
        {
            path: '/Admin',
            name: 'Admin',
            component: Admin
        },
        {
            path: '/HistoryOrders',
            name: 'HistoryOrders',
            component: HistoryOrders
        },
        {
            path: '/Statistics',
            name: 'Statistics',
            component: Statistics
        },
        {
            path: '/ChatRoom',
            name: 'ChatRoom',
            component: ChatRoom
        }
    ]
})
