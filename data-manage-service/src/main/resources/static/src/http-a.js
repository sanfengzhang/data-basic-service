import axios from "axios";
import qs from "qs";

//添加请求拦截器
axios.interceptors.request.use(config =>{return config;},error => { return Promise.reject(error);});

//添加响应拦截器
axios.interceptors.response.use(response => { return response;},error => {return Promise.resolve(error.response);});

axios.defaults.baseURL = "http://localhost:8080/";
axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.timeout = 10000;

function checkStatus(response) {
  return new Promise((resolve, reject) =>
  if (response ) {
    if(response.status === 200 ||response.status === 304 ||response.status === 400){
            resolve(response.data);
     }else if(response.status === 500){
      this.$message('消息');
    }
  } else {
    reject({
      state: "0",
      message: "网络异常"
    });
  }
});
}

export default {
  post(url, params) {
    return axios({
      method: "post",
      url,
      data: params
    }).then(response => {
      return checkStatus(response);
  });
  },
  get(url, params) {
    params = qs.stringify(params);
    return axios({
      method: "get",
      url,
      params
    }).then(response => {
      return checkStatus(response);
  });
  }
};
export function  get(url, params) {
  return new Promise((resolve, reject) =>{
    axios.get(url, {params: params}).then(res => {
      if(response.status === 500){
    this.$message('消息');
  }else{resolve(res.data)}
);
}
}).catch(err =>
{
  console.error("errorr:"+err)
});
}
