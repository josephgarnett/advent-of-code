
export type Report = {
  content: ReportEntry[];
}

export type ReportEntryType = 'title' | 'summary' | 'text';

export type ReportEntry = {
  id: string;
  type: ReportEntryType;
  content: string;
}