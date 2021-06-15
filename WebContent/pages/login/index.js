const template = /*html*/ `
  <v-container>
  
  <div class="container-login">
      <h3>Login</h3>

      <v-form class="form-login" ref="formLogin">
        <v-text-field
          placeholder="E-mail"
          v-model="email"
          type="email"
        />

        <v-text-field
          placeholder="Senha"
          v-model="senha"
          type="password"
        />
      </v-form>

      <v-btn
        :loading="loading"
        :disabled="loading"
        small
        @click="validaLogin()"
      >Entrar</v-btn>
    </div>
  </v-container>
`

export default {
	template,

	data: () => ({
		email: "",
		senha: "",

		// Rules
    campoObrigatorio: v => !!v || 'Campo obrigatório',
    
		loading: false,
  }),

	methods: {
    validaLogin() {
      let retornoValidacao = this.$refs.formLogin.validate();

      if (retornoValidacao) {
        this.login();
      }
    },

		async login() {
      this.loading = true;

      const body = {
        email: this.email,
        senha: this.senha
      }

			await axios.post("/auth", body)
			.then(retorno => {
				if(retorno) {
          const token = retorno.data.token;
          const usuario = retorno.data.usuario;
          delete usuario.senha;
          
          $auth.setToken(token);
          $auth.setUser(usuario);

					this.$router.push("/produtos");
				}
			})
			.catch(() => {
				this.$toasted.error("Erro ao realizar login");
			})
			.finally(this.loading = false);
		}
	}
}