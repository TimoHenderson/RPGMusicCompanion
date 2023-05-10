import { useState, useRef, useEffect } from 'react';

function XYpad() {
    const canvasRef = useRef(null);
    const [position, setPosition] = useState({ x: 0, y: 0 });

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

    const handleMove = (event) => {
        event.preventDefault();
        const { offsetX, offsetY } = event.nativeEvent;
        setPosition({ x: offsetX, y: offsetY });
    };

    const handleTouchMove = (event) => {
        event.preventDefault();
        const { clientX, clientY } = event.touches[0];
        const { left, top } = canvasRef.current.getBoundingClientRect();
        const offsetX = clientX - left;
        const offsetY = clientY - top;
        setPosition({ x: offsetX, y: offsetY });
    };

    return (
        <canvas
            ref={canvasRef}
            width={300}
            height={300}
            onMouseMove={handleMove}
            onTouchMove={handleTouchMove}
            onTouchStart={handleTouchMove}
        />
    );
}

export default XYpad;
