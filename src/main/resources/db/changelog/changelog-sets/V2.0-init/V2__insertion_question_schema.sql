-- ask-question for 'Date-based'
INSERT INTO public.ask_question (category, is_active, question, source_type)
VALUES
('Date-based', true, 'What was the stock''s closing price on a specific date?', 'system'),
('Date-based', true, 'What was the stock''s opening price on a specific date?', 'system'),
('Date-based', true, 'What was the highest price recorded on a given date?', 'system'),
('Date-based', true, 'What was the lowest price on a specific date?', 'system'),
('Date-based', true, 'What were the opening and closing prices on a specific date?', 'system');

-- ask-question for 'Price Movement'
INSERT INTO public.ask_question (category, is_active, question, source_type)
VALUES
('Price Movement', true, 'Did the stock price go up or down on a specific date?', 'system'),
('Price Movement', true, 'Was there any difference between the opening and closing price on a certain day?', 'system'),
('Price Movement', true, 'What was the price change (difference) between opening and closing prices on a given date?', 'system'),
('Price Movement', true, 'What was the percentage change in price on a given day?', 'system'),
('Price Movement', true, 'Did the stock close higher than it opened?', 'system');

-- ask-question for 'Volume and Open Interest'
INSERT INTO public.ask_question (category, is_active, question, source_type)
VALUES
('Volume and Open Interest', true, 'What was the trading volume on a particular date?', 'system'),
('Volume and Open Interest', true, 'How much was the open interest on a specific day?', 'system'),
('Volume and Open Interest', true, 'Was the trading volume above a certain threshold on a specific day?', 'system'),
('Volume and Open Interest', true, 'What is the significance of a high open interest?', 'system');

-- ask-question for 'Volume and Open Interest'
INSERT INTO public.ask_question (category, is_active, question, source_type)
VALUES
('Trend and Comparison', true, 'What was the price range on a particular day?', 'system'),
('Trend and Comparison', true, 'What day had the highest closing price in the dataset?', 'system'),
('Trend and Comparison', true, 'What day had the highest volume traded?', 'system'),
('Trend and Comparison', true, 'Which day showed the most volatility (difference between high and low)?', 'system'),
('Trend and Comparison', true, 'How does the price on one date compare to another?', 'system');