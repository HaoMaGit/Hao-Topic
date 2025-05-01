import "./date-picker.css";
import { attachPropertiesToComponent } from '../../utils/attach-properties-to-component';
import { DatePicker } from './date-picker';
import { TILL_NOW } from './util';
import { prompt } from './prompt';
export default attachPropertiesToComponent(DatePicker, {
  prompt,
  DATE_NOW: TILL_NOW
});