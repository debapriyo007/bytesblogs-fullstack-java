import * as React from "react"

export default function LogoBug({ className = "h-8 w-8", animated = true, variant = "default" }) {
  return (
    <div className={`relative select-none flex items-center justify-center ${className} ${animated ? "bug-animated-wrapper" : ""}`}>
      <svg
        className="w-full h-full logo-bug-svg"
        viewBox="40 30 120 120"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <style>
          {`
            .logo-bug-svg {
              transform: rotate(12deg);
              transform-origin: center;
            }
            
            @keyframes logo-float-bug {
              0%, 100% { transform: translateY(0px) rotate(2deg); }
              50% { transform: translateY(-2px) rotate(-2deg); }
            }
            @keyframes logo-leg-wiggle-left {
              0%, 100% { transform: rotate(0deg); }
              50% { transform: rotate(-5deg); }
            }
            @keyframes logo-leg-wiggle-right {
              0%, 100% { transform: rotate(0deg); }
              50% { transform: rotate(5deg); }
            }
            
            .bug-svg-animated {
              animation: logo-float-bug 4s ease-in-out infinite;
              transform-origin: center;
            }
            
            /* Leg animations on hover */
            .bug-animated-wrapper.group:hover .logo-left-leg-1 {
              animation: logo-leg-wiggle-left 0.8s ease-in-out infinite;
              transform-origin: 70px 85px;
            }
            .bug-animated-wrapper.group:hover .logo-left-leg-2 {
              animation: logo-leg-wiggle-left 0.8s ease-in-out infinite 0.15s;
              transform-origin: 70px 100px;
            }
            .bug-animated-wrapper.group:hover .logo-left-leg-3 {
              animation: logo-leg-wiggle-left 0.8s ease-in-out infinite 0.3s;
              transform-origin: 72px 115px;
            }
            
            .bug-animated-wrapper.group:hover .logo-right-leg-1 {
              animation: logo-leg-wiggle-right 0.8s ease-in-out infinite;
              transform-origin: 130px 85px;
            }
            .bug-animated-wrapper.group:hover .logo-right-leg-2 {
              animation: logo-leg-wiggle-right 0.8s ease-in-out infinite 0.15s;
              transform-origin: 130px 100px;
            }
            .bug-animated-wrapper.group:hover .logo-right-leg-3 {
              animation: logo-leg-wiggle-right 0.8s ease-in-out infinite 0.3s;
              transform-origin: 128px 115px;
            }
          `}
        </style>

        {/* Bug Group wrapper */}
        <g className={animated ? "bug-svg-animated" : ""}>
          {/* Back Legs (left side in viewBox) */}
          <path d="M70 85 C60 70, 55 80, 50 75" stroke="currentColor" className="text-zinc-400 dark:text-zinc-500 logo-left-leg-1" strokeWidth="5.5" strokeLinecap="round" />
          <path d="M70 100 C55 95, 50 105, 45 102" stroke="currentColor" className="text-zinc-400 dark:text-zinc-500 logo-left-leg-2" strokeWidth="5.5" strokeLinecap="round" />
          <path d="M72 115 C58 120, 55 130, 48 128" stroke="currentColor" className="text-zinc-400 dark:text-zinc-500 logo-left-leg-3" strokeWidth="5.5" strokeLinecap="round" />

          {/* Front Legs (right side in viewBox) */}
          <path d="M130 85 C140 70, 145 80, 150 75" stroke="currentColor" className="text-zinc-400 dark:text-zinc-500 logo-right-leg-1" strokeWidth="5.5" strokeLinecap="round" />
          <path d="M130 100 C145 95, 150 105, 155 102" stroke="currentColor" className="text-zinc-400 dark:text-zinc-500 logo-right-leg-2" strokeWidth="5.5" strokeLinecap="round" />
          <path d="M128 115 C142 120, 145 130, 152 128" stroke="currentColor" className="text-zinc-400 dark:text-zinc-500 logo-right-leg-3" strokeWidth="5.5" strokeLinecap="round" />

          {/* Antennae */}
          <path d="M92 65 C88 50, 78 45, 75 48" stroke="currentColor" className="text-zinc-500" strokeWidth="4.5" strokeLinecap="round" />
          <path d="M108 65 C112 50, 122 45, 125 48" stroke="currentColor" className="text-zinc-500" strokeWidth="4.5" strokeLinecap="round" />

          {/* Bug Body */}
          <rect x="78" y="70" width="44" height="65" rx="22" fill="#18181b" />
          
          {/* Ladybug Inverted Red Shell Wings */}
          <path d="M80 72 C65 90, 68 125, 84 133 C86 110, 88 88, 80 72 Z" fill="#e11d48" />
          <path d="M120 72 C135 90, 132 125, 116 133 C114 110, 112 88, 120 72 Z" fill="#e11d48" />

          {/* Shell Dots */}
          <circle cx="75" cy="95" r="4.5" fill="#18181b" />
          <circle cx="78" cy="115" r="3.5" fill="#18181b" />
          <circle cx="125" cy="95" r="4.5" fill="#18181b" />
          <circle cx="122" cy="115" r="3.5" fill="#18181b" />

          {/* Bug Head */}
          <circle cx="100" cy="68" r="16" fill="#18181b" />

          {/* Eyes rendering based on variant */}
          {variant === "lost" ? (
            <>
              <path d="M92 64 L96 68 M96 64 L92 68" stroke="white" strokeWidth="2.5" strokeLinecap="round" />
              <path d="M104 64 L108 68 M108 64 L104 68" stroke="white" strokeWidth="2.5" strokeLinecap="round" />
            </>
          ) : (
            <>
              <circle cx="94" cy="65" r="2.5" fill="white" />
              <circle cx="106" cy="65" r="2.5" fill="white" />
            </>
          )}
        </g>
      </svg>
    </div>
  )
}
