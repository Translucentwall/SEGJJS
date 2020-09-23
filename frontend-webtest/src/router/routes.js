import Home from '../views/Home'
import Search from '../views/SearchPage'
import Rank from '../views/Rank'
import Paper from '../views/Paper'
import AdminLogin from "../views/AdminLogin";
import Entity from "../views/Entity";
import Manage from "../views/Manage";
import Graph from "../views/Graph";

export let routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '*',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  {
    path: '/search/:mode/:content',
    name: 'Search',
    component: Search
  },
  {
    path: '/rank',
    name: 'Rank',
    component: Rank
  },
  {
    path: '/paper/:id',
    name: 'Paper',
    component: Paper
  },
  {
    path: '/login',
    name: 'AdminLogin',
    component: AdminLogin
  },
  {
    path: '/admin/manage',
    name: 'AdminManage',
    component: Manage
  },
  {
    path:'/entity/:type/:id',
    name:'Entity',
    component: Entity
  },
  {
    path: '/graph/:type/:id',
    name: 'Graph',
    component: Graph
  }
  ];
