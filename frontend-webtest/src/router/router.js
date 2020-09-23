import Vue from 'vue'
import Router from 'vue-router'
import {routes} from './routes'
import cookie from 'js-cookie';
import {authorize} from "../api/api";

Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: routes
});

router.beforeEach((to, from, next) => {
  // let adminPattern = /^\/admin/;
  // if(adminPattern.test(to.path)){
  //   let token = cookie.get('Authorization');
  //   if(token===undefined || token===''){
  //     next('/login');
  //   }else{
  //     next();
  //   }
  // }
  next();
});

export default router;
