const template = /*html*/ `
  <div>
    <m-header></m-header>

    <v-row class="mt-6 pa-4">
      <!-- TABELA -->
      <v-col cols="8">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3 class="mb-8">Lista de usuários</h3>

          <v-data-table 
            class="elevation-2"
            :headers="headers"
            :items="usuarios"
            :loading="loading"
            disable-sort
            hide-default-footer
          >
            <template v-slot:item.acoes="{ item }">
              <v-menu top close-on-click>
                <template v-slot:activator="{ on, attrs }">
                  <span
                    class="text-h5"
                    v-bind="attrs"
                    v-on="on"
                  >
                    ...
                  </span>
                </template>

                <v-list :key="item.id">
                  <v-list-item @click="deletaUsuario(item.id)">
                    <v-list-item-title >Deletar</v-list-item-title>
                  </v-list-item>

                  <v-list-item @click="ativarEditar(item.id)">
                    <v-list-item-title>Editar</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </template>
          </v-data-table>
        </v-card>
      </v-col>

      <!-- CADASTRO -->
      <v-col v-if="!editar" cols="4">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3 class="mb-8">Cadastrar usuário</h3>

          <v-form ref="formCadastro">
            <v-text-field
              v-model="nome"
              label="Nome completo"
              :rules="[campoObrigatorio]"
              placeholder=" "
            />

            <v-text-field
              v-model="email"
              label="E-mail"
              :rules="[campoObrigatorio]"
              placeholder=" "
            />
            
            <v-select
              v-model="cargo"
              label="Cargo"
              :rules="[campoObrigatorio]"
              :items="['Atendente', 'Padeiro', 'Gerente']"
            />

            <v-text-field
              type="password"
              v-model="senha"
              :rules="[campoObrigatorio]"
              label="Senha"
              placeholder=" "
            />
          </v-form>

          <v-btn
            :loading="loadingBtn"
            block
            @click="cadastraUsuario"
            color="success"btn
          >Cadastrar</v-btn>
        </v-card>
      </v-col>

      <!-- EDIÇÃO -->
      <v-col v-else cols="4">
        <v-card class="pa-8 text-left custom-border" tile>
          <span @click="voltar()" class="mdi mdi-close fecha-editar"></span>
          <h3 class="mb-8">Editar usuário</h3>

          <v-form ref="formEditar">
            <v-text-field
              v-model="nome"
              label="Nome completo"
              :rules="[campoObrigatorio]"
              placeholder=" "
            />

            <v-text-field
              v-model="email"
              label="E-mail"
              :rules="[campoObrigatorio]"
              placeholder=" "
            />
            
            <v-select
              v-model="cargo"
              label="Cargo"
              :rules="[campoObrigatorio]"
              :items="['Atendente', 'Padeiro', 'Gerente']"
            />

            <v-text-field
              type="password"
              v-model="senha"
              label="Senha"
              placeholder=" "
            />
          </v-form>

          <v-btn
            :loading="loadingBtn"
            block
            @click="editaUsuario"
            color="success"
          >Editar</v-btn>
        </v-card>
      </v-col>
    </v-row>
  </div>
`

export default {
	template,

	data: () => ({
		usuarios: [],
		nome: "",
		email: "",
		cargo: "",
		senha: "",

		headers: [
			{ text: "ID", value: "id" },
			{ text: "Nome", value: "nome" },
			{ text: "E-mail", value: "email" },
			{ text: "Cargo", value: "cargo" },
			{ text: "Ações", value: "acoes", width: "7%" }
		],

		// Rules
		campoObrigatorio: v => !!v || 'Campo obrigatório',

		dropdown: false,
		loading: false,
		loadingBtn: false,
		editar: false,
		editar_id: null
	}),

	mounted() {
		this.buscaUsuarios();
	},

	methods: {
		async buscaUsuarios() {
			this.loading = true;

			await axios.get("/usuarios")
			.then(retorno => {
        this.usuarios = retorno.data.usuarios;
			})
			.catch(err => {
				this.$toasted.error("Erro ao buscar usuários.");
			})
			.finally(this.loading = false)
		},

		async cadastraUsuario() {
			if(!this.$refs.formCadastro.validate()){
				return false;
			}
			this.loadingBtn = true;

			const {nome, email, cargo, senha} = this; 

			const body = {
				nome,
				email,
				cargo,
				senha
			}

			await axios.post("/usuarios", body)
				.then(() => {
					this.buscaUsuarios();
					this.$refs.formCadastro.reset();
				})
				.catch(() => {
					return false;
				})
				.finally(() => {
					this.loadingBtn = false;
				})
		},

		async deletaUsuario(id) {
			await axios.delete(`/usuarios/${id}`)
				.then(retorno => {
					if(retorno) {
						this.buscaUsuarios();
					}
				})
				.catch(err => {
					if(err) return false;
				})
		},

		async editaUsuario() {
			if(!this.$refs.formEditar.validate()){
				return false;
			}
			this.loadingBtn = true;

			const {nome, email, cargo, senha} = this; 

			const body = {
				nome,
				email,
				cargo,
				senha
			}

			await axios.put(`/usuarios/${this.editar_id}`, body)
				.then(retorno => {
					if(retorno) {
						this.buscaUsuarios();
						this.editar = false;
					}
				})
				.catch(err => {
					if(err) return false;
				})
				.finally(this.loadingBtn = false)
		},

		async ativarEditar(id) {
			if(id) {
				this.editar_id = id;
				this.editar = true;

				const usuarioEditar = this.usuarios.find(usuario => {
					return usuario.id == id;
				})

				this.nome = usuarioEditar.nome;
				this.email = usuarioEditar.email;
				this.cargo = usuarioEditar.cargo;
				this.senha = usuarioEditar.senha;
			}
		},

		async voltar() {
			this.editar = false;
		}
	}
}