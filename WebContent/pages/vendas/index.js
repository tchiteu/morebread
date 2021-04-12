const template = /*html*/ `
  <div>
    <m-header></m-header>

    <v-row class="mt-6 pa-4">
      <!-- TABELA -->
      <v-col cols="7">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3 class="mb-8">Últimas vendas realizadas</h3>

          <v-data-table 
            class="elevation-2"
            :headers="headers"
            :items="vendas"
            :loading="loading"
            disable-sort
            hide-default-footer
            no-data-text="Nada encontrado..."
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
      <v-col cols="5">
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
            @click="adicionaProduto"
            class="mb-1"
          >
          Adicionar produto
          </v-btn>
          
          <div v-if="produtosAdicionados.length > 0">
            <v-simple-table class="mb-2 tabela-produtos">
              <template v-slot:default>
                <thead>
                  <tr>
                    <th class="text-center">
                      Produto
                    </th>
                    <th class="text-center">
                      Quantidade
                    </th>
                    <th class="text-center">
                      Valor
                    </th>
                    <th class="text-center">
                      Remover
                    </th>
                  </tr>
                </thead>

                <tbody>
                  <tr v-for="produto in produtosAdicionados" :key="produto.id">
                    <td class="text-center">{{ produto.nome }}</td>
                    <td class="text-center">{{ produto.quantidade }}</td>
                    <td class="text-center">R$ {{ produto.valor }}</td>
                    
                    <td class="text-center">
                      <span 
                        @click="removeProduto(produto.id)"
                        class="mdi mdi-close remove-btn"
                      />
                    </td>
                  </tr>
                </tbody>
              </template>
            </v-simple-table>
          </div>

          <v-btn
            v-if="produtosAdicionados.length > 0"
            block
            @click="realizaVenda"
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
			.finally(this.loading = false);
		},

		async adicionaProduto() {
      // Validação simples
      if (this.quantidade < 1 || !this.produto.nome) {
        return false;
      }

      const produto = {
        id: this.produto.id,
        nome: this.produto.nome,
        quantidade: this.quantidade,
        valor: this.produto.valor
      }

      const index = this.produtosAdicionados.findIndex(produtoAdicionado => {
        return produtoAdicionado.id == produto.id;
      })

      if (index >= 0) {
        let quantidade = this.produtosAdicionados[index].quantidade;
        this.produtosAdicionados[index].quantidade = Number(quantidade) + Number(this.quantidade);
      }
      else {
        this.produtosAdicionados.push(produto);
      }

		},

		async removeProduto(produto_id) {
      this.produtosAdicionados.removeIf(produto => {
        return produto.id == produto_id;
      })
    },

    async realizaVenda() {
      let usuarioId = $auth.user.id
      let valorTotal = 0
      
      this.produtosAdicionados.forEach(produto => {
        valorTotal += produto.valor * produto.quantidade
      })

      const body = {
        usuarioId,
        valorTotal,
        produtos: this.produtosAdicionados
      }

      await axios.post('/vendas', body)
      .then(() => {
        this.$toasted.success("Venda realizada com sucesso!");

        // this.buscaVendas();
        this.produtosAdicionados = [];
        this.valorTotal = 0;
      })
      .catch(() => {
        this.$toasted.error("Erro ao realizar venda")
      })
    }
	}
}