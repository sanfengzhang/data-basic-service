import axios from "axios";
import qs from "qs";

//添加请求拦截器
axios.interceptors.request.use(config =>{return config;},error => { return Promise.reject(error);});

//添加响应拦截器
axios.interceptors.response.use(response => { return response;},error => {return Promise.resolve(error.response);});

axios.defaults.baseURL = "http://localhost:8080/datamgr";
axios.defaults.responseType = 'json' // 默认数据响应类型
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";
axios.defaults.headers.get["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8";
//axios.defaults.withCredentials = true
axios.defaults.timeout = 10000



 export function  post(url, body) {
    return new Promise((resolve, reject) =>{
			axios.post(url, {params: body}).then(response => {				
				if(response){
				  if(response.status === 500){
					 this.$message.error(response.data.errResult[0]);
			      }else{                    			
					resolve(response.data.data)
				  }	
				}else{
			      console.error("请求失败："+url);
				  this.$message.error('请求失败');	
				}			    
			});
			
	}).catch(err =>		
		     {
				  this.$message.error('请求失败'+err);
			      console.error("errorr:"+err)
		     });
    }				 
				 
				 
  
  
 export function  get(url, params) {
    return new Promise((resolve, reject) =>{
	  axios.get(url, {params: params}).then(response => {
				if(response){
				  if(response.status === 500){
					  this.$message.error(response.data.errResult[0]);
			      }else{                    			
					resolve(response.data)
				  }	
				}else{
				  console.error("请求失败："+url);
				  this.$message.error('请求失败');	
				}			    
			});
			
	  }).catch(err =>		
		     {
				  this.$message.error('请求失败'+err);
			      console.error("errorr:"+err)
		     });
    }
	 
  


