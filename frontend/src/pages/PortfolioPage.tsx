import React, { useState, useEffect } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { ArrowTrendingUpIcon, ArrowTrendingDownIcon } from '@heroicons/react/24/solid';
import { motion } from 'framer-motion';
import { AUTH_CONFIG } from '../config/auth';

interface Asset {
  id: number;
  symbol: string;
  name: string;
  quantity: number;
  avgBuyPrice: number;
  currentPrice: number;
  historicalData?: { date: string; value: number }[];
}

const mockHistoricalData = (basePrice: number) => {
  const data = [];
  const now = new Date();
  for (let i = 30; i >= 0; i--) {
    const date = new Date(now);
    date.setDate(date.getDate() - i);
    const randomChange = (Math.random() - 0.5) * basePrice * 0.1;
    data.push({
      date: date.toISOString().split('T')[0],
      value: basePrice + randomChange
    });
  }
  return data;
};

const PortfolioPage: React.FC = () => {
  const [assets, setAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchPortfolio = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/portfolio/user/2', {
          headers: AUTH_CONFIG.getAuthHeader()
        });
        if (!response.ok) {
          if (response.status === 401) {
            throw new Error('No autorizado. Por favor, inicia sesión nuevamente.');
          }
          throw new Error('Error al cargar el portfolio');
        }
        const data = await response.json();
        // Añadir datos históricos simulados a cada activo
        const assetsWithHistory = data.map((asset: Asset) => ({
          ...asset,
          historicalData: mockHistoricalData(asset.currentPrice)
        }));
        setAssets(assetsWithHistory);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Error desconocido');
      } finally {
        setLoading(false);
      }
    };

    fetchPortfolio();
  }, []);

  if (loading) {
    return (
      <div className="container portfolio-container" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
        <motion.div
          animate={{ rotate: 360 }}
          transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
          style={{ width: '50px', height: '50px', border: '3px solid var(--primary-color)', borderTopColor: 'transparent', borderRadius: '50%' }}
        />
      </div>
    );
  }

  if (error) {
    return (
      <div className="container portfolio-container" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="error-message"
          style={{ 
            padding: '1rem 2rem',
            backgroundColor: 'var(--danger-color)',
            color: 'white',
            borderRadius: '8px',
            textAlign: 'center'
          }}
        >
          {error}
        </motion.div>
      </div>
    );
  }

  const totalValue = assets.reduce((sum, asset) => sum + (asset.quantity * asset.currentPrice), 0);
  const totalChange = assets.reduce((sum, asset) => {
    const valueChange = (asset.currentPrice - asset.avgBuyPrice) * asset.quantity;
    return sum + valueChange;
  }, 0);
  const totalChangePercent = (totalChange / (totalValue - totalChange)) * 100;

  const portfolioHistoricalData = assets[0]?.historicalData?.map((item, index) => {
    const totalForDay = assets.reduce((sum, asset) => {
      return sum + (asset.historicalData![index].value * asset.quantity);
    }, 0);
    return {
      date: item.date,
      value: totalForDay
    };
  }) || [];

  const calculateChangePercentage = (currentPrice: number, avgBuyPrice: number): number => {
    return ((currentPrice - avgBuyPrice) / avgBuyPrice) * 100;
  };

  const calculateTotalChangePercentage = (assets: Asset[]): number => {
    const totalInvestment = assets.reduce((sum, asset) => sum + (asset.quantity * asset.avgBuyPrice), 0);
    const totalCurrentValue = assets.reduce((sum, asset) => sum + (asset.quantity * asset.currentPrice), 0);
    return ((totalCurrentValue - totalInvestment) / totalInvestment) * 100;
  };

  return (
    <motion.div 
      className="container portfolio-container"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.5 }}
    >
      <motion.div 
        className="portfolio-header"
        initial={{ y: -20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ delay: 0.2 }}
      >
        <h1 style={{ 
          fontFamily: "'Poppins', sans-serif",
          fontSize: '3rem',
          fontWeight: 700,
          letterSpacing: '-0.02em',
          background: 'linear-gradient(to right, var(--primary-color), var(--secondary-color))',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          textShadow: '0 2px 4px rgba(0,0,0,0.1)'
        }}>
          Mi Portfolio
        </h1>
      </motion.div>

      <motion.div 
        className="portfolio-stats"
        initial={{ y: 20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ delay: 0.3 }}
      >
        <motion.div 
          className="stat-card"
          whileHover={{ scale: 1.02 }}
          transition={{ type: "spring", stiffness: 300 }}
        >
          <h3>Valor Total del Portfolio</h3>
          <p>${totalValue.toLocaleString('en-US', { minimumFractionDigits: 2 })}</p>
        </motion.div>
        <motion.div 
          className="stat-card"
          whileHover={{ scale: 1.02 }}
          transition={{ type: "spring", stiffness: 300 }}
        >
          <h3>Cambio Total</h3>
          <p className={totalChange >= 0 ? 'positive' : 'negative'}>
            ${totalChange.toLocaleString('en-US', { minimumFractionDigits: 2 })}
            <span className="percentage">
              ({calculateTotalChangePercentage(assets).toFixed(2)}%)
            </span>
          </p>
        </motion.div>
      </motion.div>

      <motion.div 
        className="chart-container"
        initial={{ y: 20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ delay: 0.4 }}
      >
        <div className="chart-header">
          <div className="chart-title">Evolución del Portfolio</div>
        </div>
        <ResponsiveContainer width="100%" height={300}>
          <AreaChart data={portfolioHistoricalData}>
            <defs>
              <linearGradient id="colorValue" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#6366f1" stopOpacity={0.3}/>
                <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
            <XAxis 
              dataKey="date" 
              stroke="#94a3b8"
              tick={{ fill: '#94a3b8' }}
            />
            <YAxis 
              stroke="#94a3b8"
              tick={{ fill: '#94a3b8' }}
              tickFormatter={(value) => `$${value.toLocaleString()}`}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: '#1e293b',
                border: '1px solid #334155',
                borderRadius: '8px',
                color: '#e2e8f0'
              }}
            />
            <Area
              type="monotone"
              dataKey="value"
              stroke="#6366f1"
              fillOpacity={1}
              fill="url(#colorValue)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </motion.div>

      <motion.div 
        className="assets-grid"
        initial={{ y: 20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ delay: 0.5 }}
      >
        {assets.map((asset, index) => {
          const value = asset.quantity * asset.currentPrice;
          const change = (asset.currentPrice - asset.avgBuyPrice) * asset.quantity;
          const changePercentage = calculateChangePercentage(asset.currentPrice, asset.avgBuyPrice);

          return (
            <motion.div 
              key={asset.id}
              className="asset-card"
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.6 + index * 0.1 }}
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
            >
              <div className="asset-header">
                <div>
                  <div className="asset-name">{asset.name}</div>
                  <div className="asset-symbol">{asset.symbol}</div>
                </div>
                <div className={`asset-price ${change >= 0 ? 'positive' : 'negative'}`}>
                  ${asset.currentPrice.toLocaleString('en-US', { minimumFractionDigits: 2 })}
                  <span className="percentage">
                    ({changePercentage.toFixed(2)}%)
                  </span>
                  {change >= 0 ? (
                    <ArrowTrendingUpIcon style={{ width: '20px', height: '20px', marginLeft: '4px', display: 'inline' }} />
                  ) : (
                    <ArrowTrendingDownIcon style={{ width: '20px', height: '20px', marginLeft: '4px', display: 'inline' }} />
                  )}
                </div>
              </div>

              <div style={{ height: '100px', marginBottom: '1rem' }}>
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={asset.historicalData}>
                    <defs>
                      <linearGradient id={`color${asset.id}`} x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor={change >= 0 ? '#10b981' : '#ef4444'} stopOpacity={0.3}/>
                        <stop offset="95%" stopColor={change >= 0 ? '#10b981' : '#ef4444'} stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <Area
                      type="monotone"
                      dataKey="value"
                      stroke={change >= 0 ? '#10b981' : '#ef4444'}
                      fillOpacity={1}
                      fill={`url(#color${asset.id})`}
                    />
                  </AreaChart>
                </ResponsiveContainer>
              </div>

              <div className="asset-details">
                <div className="asset-detail">
                  <span>Cantidad:</span>
                  <span>{asset.quantity}</span>
                </div>
                <div className="asset-detail">
                  <span>Valor Total:</span>
                  <span>${value.toLocaleString('en-US', { minimumFractionDigits: 2 })}</span>
                </div>
                <div className="asset-detail">
                  <span>Cambio:</span>
                  <span className={change >= 0 ? 'positive' : 'negative'}>
                    ${change.toLocaleString('en-US', { minimumFractionDigits: 2 })}
                    <span className="percentage">
                      ({changePercentage.toFixed(2)}%)
                    </span>
                  </span>
                </div>
                <div className="asset-detail">
                  <span>Precio Medio:</span>
                  <span>${asset.avgBuyPrice.toLocaleString('en-US', { minimumFractionDigits: 2 })}</span>
                </div>
              </div>
            </motion.div>
          );
        })}
      </motion.div>
    </motion.div>
  );
};

export default PortfolioPage; 