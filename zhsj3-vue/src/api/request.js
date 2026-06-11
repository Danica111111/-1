import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 10000
})

request.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        console.error(error)
        alert('请求失败，请检查后端是否启动')
        return Promise.reject(error)
    }
)

export default request