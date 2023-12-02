import { useState } from 'react'

import githubIcon from './assets/github.png';

function App() {
  const [count, setCount] = useState(0)

  return (
    <header>
      <a class="icon" href="https://github.com/josephgarnett/advent-of-code">
        <img src={githubIcon} alt="Github link" />
      </a>
      <h1>Advent of code 2023</h1>
    </header>
  )
}

export default App
