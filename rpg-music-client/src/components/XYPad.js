import { useState, useEffect, useRef } from "react";

const XYPad = ({ sendPadMessage }) => {
    const [position, setPosition] = useState({ x: 1, y: 1 });
    const [isDragging, setIsDragging] = useState(false);
    const canvasRef = useRef(null);




    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');

        // draw the XY pad
        ctx.fillStyle = '#eee';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        // draw the cursor
        ctx.fillStyle = '#f00';
        ctx.beginPath();
        ctx.arc(position.x, position.y, 10, 0, Math.PI * 2);
        ctx.fill();
    }, [position]);

    const buildPadMessage = (position) => {

        const convertedX = Math.ceil(position.x * 5);
        const convertedY = Math.ceil(position.y * 5);

        const message = JSON.stringify({
            event: "PARAMS",
            darkness: convertedX,
            intensity: convertedY,
        });

        sendPadMessage(message);
    };

    const handleDragStart = (event) => {
        event.preventDefault();
        setIsDragging(true);
    };

    const handleDragEnd = (event) => {
        event.preventDefault();
        setIsDragging(false);
    };

    const handleDragMove = (event) => {
        event.preventDefault();
        if (isDragging) {
            const rect = canvasRef.current.getBoundingClientRect();
            const padX = event.clientX - rect.left;
            const padY = event.clientY - rect.top;
            setPosition({ x: padX, y: padY })
            const message = buildMessage(position);

        }
    };

    const buildMessage = (x, y) => {
        const message = JSON.stringify({
            event: "PARAMS",
            darkness: x,
            intensity: y,
        });
        return message;
    };

    useEffect(() => {
        const handleTouchMove = (event) => {
            handleDragMove(event.touches[0]);
        };

        if (isDragging) {
            document.addEventListener("mousemove", handleDragMove);
            document.addEventListener("mouseup", handleDragEnd);
            document.addEventListener("touchmove", handleTouchMove);
            document.addEventListener("touchend", handleDragEnd);
        } else {
            document.removeEventListener("mousemove", handleDragMove);
            document.removeEventListener("mouseup", handleDragEnd);
            document.removeEventListener("touchmove", handleTouchMove);
            document.removeEventListener("touchend", handleDragEnd);
        }
    }, [isDragging]);

    const hue = position.x * 360;
    const saturation = position.y * 100;
    const lightness = 50;

    return (
        <div
            className="xy-pad"
            style={{
                backgroundColor: `hsl(${hue}, ${saturation}%, ${lightness}%)`,
            }}>
            <canvas
                ref={canvasRef}
                width={300}
                height={300}
                onMouseDown={handleDragStart}
                onTouchStart={handleDragStart}
            />
        </div>
    );
}
export default XYPad;
