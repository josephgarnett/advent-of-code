(function () {
    fetch('report.txt')
        .then((response) => {
          return response.text();
        })
        .then((report) => {
           document.getElementById('#root')
            .textContent = report;
        });
})();