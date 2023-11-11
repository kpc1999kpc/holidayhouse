import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { ContextProvider } from './contexts/ContextProvider'
import { registerLicense } from '@syncfusion/ej2-base';
import { L10n, setCulture } from '@syncfusion/ej2-base';
import * as pl from '@syncfusion/ej2-locale/src/pl.json';

registerLicense('ORg4AjUWIQA/Gnt2VlhhQlJCfV5AQmBIYVp/TGpJfl96cVxMZVVBJAtUQF1hSn9TdUVjXnxbc3VQRGBa');
setCulture('pl');
L10n.load(pl);

ReactDOM.render(
  <ContextProvider>
    <App />
  </ContextProvider>,
  document.getElementById('root'),
);
