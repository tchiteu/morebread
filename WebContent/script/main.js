import Login from '../pages/login/index.js';
import Usuarios from '../pages/usuarios/index.js';
import Produtos from '../pages/produtos/index.js';
import Vendas from '../pages/vendas/index.js';

import GlobalMethods from '../plugins/globalMethods.js';

axios.defaults.baseURL = 'http://localhost:8080/morebread/rest';

const router = new VueRouter({
  routes: [
		{ path: '/login', component: Login },
		{ path: '/usuarios', component: Usuarios },
		{ path: '/produtos', component: Produtos },
		{ path: '/vendas', component: Vendas }
  ]
})

import Header from '../components/general/Header.js';

Vue.component('m-header', Header);

Vue.use(Toasted);
Vue.use(VueCurrencyInput);
Vue.use(GlobalMethods);

Vue.toasted.register('error',
	{
		type: 'error',
		duration: 3000
	}
)

Vue.toasted.register('success',
	{
		type: 'success',
		duration: 3000
	}
)

// Prototypes
String.prototype.insert = function(index, string) {
  if (index > 0) {
    return this.substring(0, index) + string + this.substr(index);
  }

  return string + this;
};

Array.prototype.removeIf = function(callback) {
	let i = 0;

	while (i < this.length) {
		if (callback(this[i], i)) {
			this.splice(i, 1);
		}
		else {
			++i;
		}
	}
};

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
	},
	methods: {
		hello() {
			console.log("boooooooooooooooooraaaaaaaaaaaaa")
		}
	}
}).$mount('#app')