module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  darkMode: 'class',
  theme: {
    fontFamily: {
      display: ['Open Sans', 'sans-serif'],
      body: ['Open Sans', 'sans-serif'],
    },
    extend: {
      colors: {
        pastelbrown: '#c8bea0'
      },
      fontSize: {
        14: '14px',
      },
      backgroundColor: {
        'main-bg': '#F4F6F6',
        'main-dark-bg': '#20232A',
        'secondary-dark-bg': '#33373E',
        'light-gray': '#F7F7F7',
        'light-pink': '#FFD6E2',
        'dark-pink': '#E8C3D0',
        'light-blue': '#E7EBFD',
        'half-transparent': 'rgba(0, 0, 0, 0.5)',
        
      },
      borderWidth: {
        1: '1px',
      },
      borderColor: {
        color: 'rgba(0, 0, 0, 0.1)',
      },
      width: {
        400: '400px',
        760: '760px',
        780: '780px',
        800: '800px',
        1000: '1000px',
        1200: '1200px',
        1400: '1400px',
      },
      height: {
        80: '80px',
      },
      minHeight: {
        590: '590px',
      },
      backgroundImage: {
        'hero-pattern':
          "url('../src/data/Background.jpg')",
      },
    },
  },
  plugins: [],
};