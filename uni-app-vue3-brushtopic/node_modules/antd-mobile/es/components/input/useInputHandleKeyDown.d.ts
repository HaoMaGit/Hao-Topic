interface InputHandleKeyDownType<T> {
    onEnterPress?: (e: React.KeyboardEvent<T>) => void;
    onKeyDown?: (e: React.KeyboardEvent<T>) => void;
    enterKeyHint?: React.InputHTMLAttributes<HTMLInputElement>['enterKeyHint'];
    nativeInputRef: React.RefObject<T>;
}
export default function useInputHandleKeyDown<T extends HTMLInputElement | HTMLTextAreaElement>({ onEnterPress, onKeyDown, nativeInputRef, enterKeyHint, }: InputHandleKeyDownType<T>): (e: React.KeyboardEvent<T>) => void;
export {};
