import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { nanoid } from '@reduxjs/toolkit';

import { Report, ReportEntryType } from '../types/Report';

type GetReportRequest = {
  year: '2023';
};

export const api = createApi({
  baseQuery: fetchBaseQuery({baseUrl: ''}),
  endpoints: (builder) => ({
    getReport: builder.query<Report, GetReportRequest>({
      query: ({year}) => ({
        url: `/data/${year}/report.txt`,
        responseHandler: (res) => res.text()
      }),
      transformResponse: (response: string) => ({
        content: response
          .split("\n")
          .map((line) => {
            let type: ReportEntryType = 'text';
            const content = line
              .replace(/.\[\d+m/g, '')
              .replace(/.\[m/g, "");

            if (content.startsWith('---')) {
              type = 'title';
            } else if (content.startsWith('🎉')) {
              type = 'summary';
            }

            return {
              id: nanoid(),
              type,
              content
            };
          })
      })
    })
  })
});

export const { useGetReportQuery } = api;