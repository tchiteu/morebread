const template = /*html*/ `
  <div>
    <m-header></m-header>

    <v-row class="mt-6 pa-4">
      <!-- TABELA -->
      <v-col cols="8">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3 class="mb-8">Lista de produtos</h3>

          <v-data-table 
            class="elevation-2"
            :headers="headers"
            :items="produtos"
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
                  <v-list-item @click="deletaProduto(item.id)">
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
          <h3 class="mb-8">Cadastrar produto</h3>

          <v-form ref="formCadastro">
						<v-text-field
							v-model="nome"
							label="Nome"
							:rules="[campoObrigatorio]"
							placeholder="  "
						/>

						<v-text-field
							v-model="valor"
							label="Valor"
							:rules="[campoObrigatorio]"
							placeholder="  "
							v-currency="{ currency: null,locale: 'pt',autoDecimalMode: true }"
						/>

						<v-text-field
							type="number"
							v-model="quantidade"
							label="Quantidade"
							:rules="[campoObrigatorio]"
							placeholder="  "
						/>
          </v-form>

          <v-btn
            :loading="loadingBtn"
            block
            @click="cadastraProduto"
            color="success"btn
          >Cadastrar</v-btn>
        </v-card>
      </v-col>

      <!-- EDIÇÃO -->
      <v-col v-else cols="4">
        <v-card class="pa-8 text-left custom-border" tile>
          <span @click="voltar()" class="mdi mdi-close fecha-editar"></span>
          <h3 class="mb-8">Editar produto</h3>

          <v-form ref="formEditar">
						<v-text-field
							v-model="nome"
							label="Nome"
							:rules="[campoObrigatorio]"
							placeholder="  "
						/>

						<v-text-field
							v-model="valor"
							label="Valor"
							:rules="[campoObrigatorio]"
							placeholder="  "
							v-currency="{ currency: null,locale: 'pt',autoDecimalMode: true }"
						/>

						<v-text-field
							type="number"
							v-model="quantidade"
							label="Quantidade"
							:rules="[campoObrigatorio]"
							placeholder="  "
						/>
					</v-form>

					<v-btn
						:loading="loadingBtn"
						block
						@click="editaProduto"
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
		produtos: [],
		nome: "",
		valor: 0.00,
		quantidade: 1,

		headers: [
			{ text: "ID", value: "id" },
			{ text: "Nome", value: "nome" },
			{ text: "Valor", value: "valor_formatado" },
			{ text: "Quantidade", value: "quantidade" },
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
		this.buscaProdutos();
	},

	methods: {
		async buscaProdutos() {
			this.loading = true;

			await axios.get("/produtos")
			.then(retorno => {
				let produtos = retorno.data.produtos;

				this.produtos = produtos.map(produto => {
					let valor = produto.valor.toFixed(2).toString();
					valor = valor.replace(".", ",");
					
					if (produto.valor > 9999) {
						valor = valor.insert(2, ".");
					}
 					else if (produto.valor > 999) {
						valor = valor.insert(1, ".");
					}

					produto.valor_formatado = `R$ ${valor}`;
					return produto;
				})
			})
			.catch(err => {
				this.$toasted.error("Erro ao buscar produtos");
			})
			.finally(this.loading = false)
		},

		async cadastraProduto() {
			if(!this.$refs.formCadastro.validate()){
				return false;
			}
			this.loadingBtn = true;

			let { nome, valor, quantidade } = this; 

			valor = valor.replace(".", "");
			valor = valor.replace(",", ".");

			const body = {
				nome,
				valor: parseFloat(valor).toFixed(2),
				quantidade: parseInt(quantidade)
			}

			await axios.post("/produtos", body)
				.then(() => {
					this.buscaProdutos();
					this.$refs.formCadastro.reset();
				})
				.catch(() => {
					return false;
				})
				.finally(() => {
					this.loadingBtn = false;
				})
		},

		async deletaProduto(id) {
			await axios.delete(`/produtos/${id}`)
				.then(retorno => {
					if(retorno) {
						this.buscaProdutos();
					}
				})
				.catch(err => {
					if(err) return false;
				})
		},

		async editaProduto() {
			if(!this.$refs.formEditar.validate()){
				return false;
			}
			this.loadingBtn = true;

			let { nome, valor, quantidade } = this; 

			valor = valor.replace(".", "");
			valor = valor.replace(",", ".");

			const body = {
				nome,
				valor: parseFloat(valor).toFixed(2),
				quantidade: parseInt(quantidade)
			}

			await axios.put(`/produtos/${this.editar_id}`, body)
				.then(retorno => {
					if(retorno) {
						this.buscaProdutos();
						this.voltar();
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

				const produtoEditar = this.produtos.find(produto => {
					return produto.id == id;
				})

				this.nome = produtoEditar.nome;
				this.valor = produtoEditar.valor;
				this.quantidade = produtoEditar.quantidade;
			}
		},

		async voltar() {
			this.editar = false;
			setTimeout(() => {
				this.$refs.formCadastro.reset();
				this.$refs.formCadastro.resetValidation();
			}, 500)
		}
	}
}