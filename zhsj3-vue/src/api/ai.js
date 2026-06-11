import request from './request'

export function recommendCar(data) {
    return request.post('/ai/recommend', data)
}