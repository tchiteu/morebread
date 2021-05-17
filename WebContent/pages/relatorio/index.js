const template = /*html*/ `
  <div>
    <m-header></m-header>

    <v-row class="pa-4 justify-center">
      <v-col cols="4"> 
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="datas"
          transition="scale-transition"
          offset-y
          min-width="auto"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="datasTexto"
              label="Período"
              placeholder="Selecione a data de início"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>
          
          <v-date-picker v-model="datas" range locale="pt-br" no-title @blur="$refs.menu.save(datas)" />
        </v-menu>
      </v-col>

      <v-col cols="7" class="d-flex align-center justify-end">
        <v-btn 
          @click="geraRelatorio"
          color="success"
          :disabled="!datas[1]"
        >
          Gerar Relatório
        </v-btn>
      </v-col>
    </v-row>

    <v-row class="pa-4 justify-center">
      <!-- TABELA -->
      <v-col cols="11">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3>Relatório vendas</h3>
          <small>Detalhes de cada produto vendido dentro de um período</small>
          
          <v-data-table 
            class="elevation-2 mt-3"
            :headers="headers"
            :items="detalhesVendas"
            :loading="carregando"
            disable-sort
            hide-default-footer
            loading-text="Buscando..."
            no-data-text="Nada encontrado..."
          >
            <template v-slot:item.dataRealizada="{ item }">
              {{ formataData(item.dataRealizada) }}
            </template>

            <template v-slot:item.valor="{ item }">
              R$ {{ item.valor.toFixed(2) }}
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </div>
`

export default {
	template,

	data: () => ({
    datas: [],
    datasTexto: "",
    menu: false,
		detalhesVendas: [],
    carregando: false,
    
    headers: [
      { text: "Venda ID", value: "vendaId" },
			{ text: "Data", value: "dataRealizada" },
			{ text: "Usuário", value: "usuarioNome" },
			{ text: "Produto", value: "produtoNome" },
			{ text: "Quantidade", value: "quantidade" },
			{ text: "Valor", value: "valor" }
    ],

		// Rules
		campoObrigatorio: v => !!v || 'Campo obrigatório'
	}),
	methods: {
    async geraRelatorio() {
      this.carregando = true;

      await axios.get(`/relatorios/vendas?dt_inicio=${this.datas[0]}&dt_fim=${this.datas[1]}`)
      .then(retorno => {
        this.detalhesVendas = retorno.data.detalhesVendas;
      })
      .catch(() => {
        this.$toasted.error("Erro ao buscar relatório");
      })
      .finally(() => this.carregando = false);
    },

    salvaDatas() {
      this.datas = this.datas
    }
  },
  watch: {
    datas(novasDatas) {
      if (novasDatas.length == 1) {
        this.datasTexto = this.formataData(novasDatas[0]);
      }

      if (novasDatas.length == 2) {
        let data_inicio = this.formataData(novasDatas[0]);
        let data_fim = this.formataData(novasDatas[1]);

        this.datasTexto = `${data_inicio} - ${data_fim}`;
        this.$refs.menu.save(novasDatas);
      }
    }
  }
}