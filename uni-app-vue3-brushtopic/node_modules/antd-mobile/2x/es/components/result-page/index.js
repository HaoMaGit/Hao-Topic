import "./result-page.css";
import { ResultPage } from './result-page';
import { attachPropertiesToComponent } from '../../utils/attach-properties-to-component';
import { ResultPageCard } from './result-page-card';
export default attachPropertiesToComponent(ResultPage, {
  Card: ResultPageCard
});