import { createBrowserRouter } from "react-router-dom";

import { AppRoute } from "./app";
import { AdventOfCodeRoute } from "./adventOfCode";

export const router = createBrowserRouter([{
  path: "/",
  element: <AppRoute />,
  children: [{
    path: "/:year?",
    element: <AdventOfCodeRoute />,
  }]
}]);