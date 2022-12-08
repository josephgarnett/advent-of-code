(function () {

    const COLOR_PATTERN = /^.\[\d+m.*/gi
    const TERMINATOR_PATTERN = /^.\[m/gi
    fetch('report.txt')
        .then((response) => {
          return response.text();
        })
        .then((report) => {
           const lines = report.split('\n');

           const list = document.createElement('ul');

           lines.forEach((line) => {
             const li = document.createElement('li');

             if (COLOR_PATTERN.test(line) || TERMINATOR_PATTERN.test(line)) {
              line = line.replace(/.\[\d+m/g, '');
              line = line.replace(/.\[m/g, "")
             }

             if (line.startsWith('---')) {
               li.classList.add('title');
             }
             li.textContent = line;
             list.appendChild(li);
           });

           document.getElementById('root').appendChild(list);
        });
})();