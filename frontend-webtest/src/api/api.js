import axios from 'axios';
import cookie from 'js-cookie'

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
axios.defaults.baseURL = process.env.NODE_ENV === 'production'
  ? 'http://' + location.hostname +':80/api'
  : 'http://localhost:8080/api';

export const searchable = () =>{
  return axios.get('/searchable').then(res => res.data);
};
export const getPaper = (id) =>{
  return axios.get('/paperDetail/'+ id).then(res => res.data);
};

export const search = (text, mode, pageNumber, sortMode, perPage) =>{
  return axios.get('/search/' + encodeURIComponent(text) + '/' + mode + '?pageNumber=' + pageNumber + '&sortMode=' + sortMode + '&perPage=' + perPage).then(res => res.data);
};

export const getRank = (mode, pageNumber, descend, startYear, endYear) =>{
  return axios.get('/rank/' + mode + '?pageNumber=' + pageNumber + '&descend=' + descend + '&startYear=' + startYear + '&endYear=' + endYear).then(res => res.data);
};

export const getPopRank = (type)=>{
  return axios.get('/hot?type='+type).then(res=>res.data);
};

export const login = (username, password, remember_me) =>{
  let data = {};
  return axios.post('/login?remember-me='+remember_me,{username:username,password:password}).then(res=>res.data);
};

export const logout = () => {
  return axios.post('/logout').then(res=>res.data);
};

export const getConfusedAlias = (page, type) => {
  let authorize = cookie.get('Authorization');
    return axios.get('/admin/getConfusedAlias?page='+page+'&type='+type, (authorize===undefined?'':{headers:{'Authorization': 'Bearer '+authorize}})).then(res=>res.data);
};

export const getEffectiveAlias = (page, type) => {
  let authorize = cookie.get('Authorization');
  return axios.get('/admin/getEffectiveAlias?page='+page+'&type='+type, (authorize===undefined?'':{headers:{'Authorization': 'Bearer '+authorize}})).then(res=>res.data);
};

export const modifyAlias = (sonId, fatherId, type) => {
  let authorize = cookie.get('Authorization');
  return axios.post('/admin/modifyAlias?sonId='+sonId+'&fatherId='+fatherId+'&type='+type,{},(authorize===undefined?'':{headers:{'Authorization': 'Bearer '+authorize}})).then(res=>res.data);
};

export const cancelAlias = (sonId, type) => {
  let authorize = cookie.get('Authorization');
  return axios.post('/admin/cancelAlias?sonId='+sonId+'&type='+type,{},(authorize===undefined?'':{headers:{'Authorization': 'Bearer '+authorize}})).then(res=>res.data);
};

export const getAcademicEntity = (id, type) =>{
  return axios.get('/academic/' + id + '?type=' + type).then(res=>res.data);
};

export const getSignificantPaper = (id, type, year, termId) =>{
  return axios.get('/academic/significantPapers?year=' + year + '&termId=' + termId + '&id=' + id + '&type=' + type).then(res=>res.data);
};

export const getGraph = (id, type) =>{
  return axios.get('/graph/basic/' + id + '?type=' + type).then(res=>res.data);
};

export const getMoreGraph = (id, type) =>{
  return axios.get('/graph/more/' + id + '?type=' + type).then(res=>res.data);
};
