const template = /*html*/ `
  <div>
    <m-header></m-header>

    <v-row class="mt-6 pa-4">
      <!-- TABELA -->
      <v-col cols="8">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3 class="mb-8">Últimas vendas realizadas</h3>

          <v-data-table 
            class="elevation-2"
            :headers="headers"
            :items="vendas"
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
                  <v-list-item @click="deletaVenda(item.id)">
                    <v-list-item-title >Deletar</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </template>
          </v-data-table>
        </v-card>
      </v-col>

      <!-- CADASTRO -->
      <v-col cols="4">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3 class="mb-8">Realizar venda</h3>

          <v-form ref="formProduto" class="mb-3">
            <v-row dense>
              <v-col cols="8">
                <v-autocomplete
                  v-model="produto"
                  label="Selecione um produto"
                  :items="produtos"
                  item-text="nome"
                  :return-object="true"
                  :rules="[campoObrigatorio]"
                  placeholder=" "
                />
              </v-col>

              <v-col cols="4">
                <v-text-field
                  type="number"
                  v-model="quantidade"
                  label="Quantidade"
                  :rules="[campoObrigatorio]"
                  placeholder=" "
                />
              </v-col>
            </v-row>
          </v-form>

          <v-btn 
            block
            @click="() => adicionarProduto()"
          >
          Adicionar produto
          </v-btn>
          
          <div v-if="produtosAdicionados.length > 0">
            <v-simple-table>
              <template v-slot:default>
                <thead>
                  <tr>
                    <th class="text-left">
                      Produto
                    </th>
                    <th class="text-left">
                      Quantidade
                    </th>
                    <th class="text-left">
                      Valor
                    </th>
                    <th class="text-left">
                      
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="produto in produtosAdicionados" :key="produto.id"
                  >
                    <td>{{ produto.nome }}</td>
                    <td>{{ produto.quantidade }}</td>
                    <td>{{ produto.valor }}</td>
                    <td> X </td>
                  </tr>
                </tbody>
              </template>
            </v-simple-table>
          </div>

          <v-btn
            block
            color="success"
          >Salvar</v-btn>
        </v-card>
      </v-col>
    </v-row>
  </div>
`

export default {
	template,

	data: () => ({
		vendas: [],
    produto: [],
    produtos: [],
    produtosAdicionados: [],
    quantidade: null,
    
    headers: [
      { text: "ID", value: "id" },
			// { text: "Nome", value: "nome" },
			// { text: "Valor", value: "valor_formatado" },
			// { text: "Quantidade", value: "quantidade" },
    ],

		// Rules
		campoObrigatorio: v => !!v || 'Campo obrigatório',

		loading: false,
	}),

	mounted() {
		this.buscaProdutos();
	},

	methods: {
		async buscaProdutos() {
			this.loading = true;

			await axios.get("/produtos")
			.then(retorno => {
        this.produtos = retorno.data.produtos;
			})
			.catch(() => {
				this.$toasted.error("Erro ao buscar produtos.");
			})
			.finally(this.loading = false)
		},

		async adicionarProduto() {
      const produto = {
        id: this.produto.id,
        nome: this.produto.nome,
        quantidade: this.quantidade,
        valor: this.produto.valor
      }
      
      const index = this.produtosAdicionados.findIndex(produtoAdicionado => {
        produtoAdicionado.id == produto.id
      })

      if (index >= 0) {
        this.produtosAdicionados[index].quantidade += this.quantidade
      }
      else {
        this.produtosAdicionados.push(produto);
      }

		},

		async removerProduto(id) {
    }
	}
}