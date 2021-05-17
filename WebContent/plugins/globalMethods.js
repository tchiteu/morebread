const GlobalMethods = {}

GlobalMethods.install = (Vue) => {
  Vue.mixin({
    methods: {
      formataData(data) {
        let dataArray = data.split('-');
        let ano = dataArray[0];
        let mes = dataArray[1];
        let dia = dataArray[2];
      
        return `${dia}/${mes}/${ano}`;
      }
    }
  })

}

export default GlobalMethods
