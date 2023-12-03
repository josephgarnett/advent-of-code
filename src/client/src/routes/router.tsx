import { createHashRouter } from "react-router-dom";

import { AppRoute } from "./app";
import { AdventOfCodeRoute } from "./adventOfCode";

export const router = createHashRouter([{
  path: "/",
  element: <AppRoute />,
  children: [{
    path: "/:year?",
    element: <AdventOfCodeRoute />,
  }]
}]);