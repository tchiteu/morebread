import Login from '../pages/login/index.js';
import Usuarios from '../pages/usuarios/index.js';

axios.defaults.baseURL = 'http://localhost:8080/morebread/rest';

axios.interceptors.request.use((config) => {
	const token = localStorage.getItem("token");
	config.headers.Authorization =  token;

	return config;
});

const router = new VueRouter({
  routes: [
		{ path: '/login', component: Login },
		{ path: '/usuarios', component: Usuarios }
  ]
})

import Header from '../components/general/Header.js';

Vue.component('m-header', Header);

Vue.use(Toasted)

Vue.toasted.register('error',
	(payload) => {
		return payload
	},
	{
		type: 'error',
		duration: 4000
	}
)

Vue.toasted.register('success',
	(payload) => {
		return payload
	},
	{
		type: 'success',
		duration: 3000
	}
)

new Vue({
	router,
	el: '#app',
	vuetify: new Vuetify({
		theme: {
			themes: {
				light: {
					primary: '#212121',
					secondary: '#424242',
					background: '#F5F5F5',
					success: '#81C784',
					info: '#64B5F6',
					warning: '#FFB300',
					error: '#E57373',
					default: '#E0E0E0',
				}
			},
			options: {
				customProperties: true
			},
		},
	}),
	data() {
		return {
		}
	}
}).$mount('#app')