(function () {
    fetch('report.txt')
        .then((response) => {
          return response.text();
        })
        .then((report) => {
           console.log(report);
        });
})();