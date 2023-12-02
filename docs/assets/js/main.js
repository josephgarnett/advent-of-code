(function () {
    function isOverflowing(el) {
      return el.scrollWidth > el.offsetWidth;
    }
    const COLOR_PATTERN = /^.\[\d+m.*/gi
    const TERMINATOR_PATTERN = /^.\[m/gi
    fetch('./assets/data/report.txt')
        .then((response) => {
          return response.text();
        })
        .then((report) => {
           const lines = report.split('\n');

           const list = document.createElement('ul');

           lines.forEach((line, i) => {
             const li = document.createElement('li');
             let isExpanded = false;

             if (COLOR_PATTERN.test(line) || TERMINATOR_PATTERN.test(line)) {
              line = line.replace(/.\[\d+m/g, '');
              line = line.replace(/.\[m/g, "")
             }

             if (line.startsWith('---')) {
               li.classList.add('title');
             }

             if (line.startsWith('🎉')) {
               li.classList.add('summary');
             }

             li.textContent = line;

             li.onclick = (e) => {
               if (isOverflowing(li)) {
                if (isExpanded) {
                  li.replaceChildren();
                  li.textContent = line;
                  isExpanded = false;
                } else {
                  const text = document.createElement('pre');
                  text.classList.add('overflow')
                  text.textContent = line;
                  li.appendChild(text);
                  isExpanded = true;
                }
               }
             }

             list.appendChild(li);
           });

           document.getElementById('root').appendChild(list);
           [...document.getElementsByTagName('li')]
            .forEach((node) => node.classList.toggle('overflow', isOverflowing(node)))
        });
})();