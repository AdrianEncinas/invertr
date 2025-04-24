import React from 'react';

interface AssetCardProps {
  symbol: string;
  name: string;
  quantity: number;
  avgBuyPrice: number;
  currentPrice: number;
  change: number;
  changePercent: number;
}

const AssetCard: React.FC<AssetCardProps> = ({
  symbol,
  name,
  quantity,
  avgBuyPrice,
  currentPrice,
  change,
  changePercent
}) => {
  const totalValue = quantity * currentPrice;
  const profitLoss = (currentPrice - avgBuyPrice) * quantity;
  const profitLossPercent = ((currentPrice - avgBuyPrice) / avgBuyPrice) * 100;

  return (
    <div className="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition-shadow duration-300">
      <div className="flex justify-between items-start">
        <div>
          <h3 className="text-xl font-bold text-gray-800">{symbol}</h3>
          <p className="text-gray-600">{name}</p>
        </div>
        <div className={`text-lg font-semibold ${change >= 0 ? 'text-green-600' : 'text-red-600'}`}>
          {changePercent.toFixed(2)}%
        </div>
      </div>

      <div className="mt-4 space-y-2">
        <div className="flex justify-between">
          <span className="text-gray-600">Cantidad:</span>
          <span className="font-medium">{quantity.toFixed(4)}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-gray-600">Precio promedio:</span>
          <span className="font-medium">${avgBuyPrice.toFixed(2)}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-gray-600">Precio actual:</span>
          <span className="font-medium">${currentPrice.toFixed(2)}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-gray-600">Valor total:</span>
          <span className="font-medium">${totalValue.toFixed(2)}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-gray-600">Ganancia/Pérdida:</span>
          <span className={`font-medium ${profitLoss >= 0 ? 'text-green-600' : 'text-red-600'}`}>
            ${profitLoss.toFixed(2)} ({profitLossPercent.toFixed(2)}%)
          </span>
        </div>
      </div>
    </div>
  );
};

export default AssetCard; 