import { useLayoutEffect, useRef, useState } from "react";
import { clsx } from 'clsx';

import { ReportEntry as ReportEntryProps } from '../../types/Report';

export function ReportEntry(props: ReportEntryProps) {
  const itemRef = useRef<HTMLLIElement>(null);
  const [isExpanded, setIsExpanded] = useState(false);
  const [isOverflowing, setIsOverflowing] = useState(false);

  useLayoutEffect(() => {
    const { current: el } = itemRef;

    if(el) {
      setIsOverflowing(el.scrollWidth > el.offsetWidth);
    }
  });

  return (
    <li
      ref={itemRef}
      className={clsx(
        "report-entry",
        props.type,
        isOverflowing && 'overflow'
      )}
      onClick={() => {
        if (!isOverflowing) {
          return;
        }

        setIsExpanded((current) => !current);
      }}
    >
      {props.content}
      {isExpanded && (
        <pre className="overflow">
          {props.content}
        </pre>
      )}
    </li>
  );
}